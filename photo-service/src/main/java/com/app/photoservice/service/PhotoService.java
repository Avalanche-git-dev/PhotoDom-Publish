package com.app.photoservice.service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.app.photoservice.dto.PhotoDto;
import com.app.photoservice.dto.PhotoMapper;
import com.app.photoservice.entity.Like;
import com.app.photoservice.entity.PhotoMetadata;
import com.app.photoservice.exception.PhotoNotFoundException;
import com.app.photoservice.exception.PhotoReadingException;
import com.app.photoservice.kafka.PhotoKafkaProducer;
import com.app.photoservice.kafka.event.PhotoEvent;
import com.app.photoservice.repository.LikeRepository;
import com.app.photoservice.repository.PhotoMetadataRepository;
import com.app.photoservice.socket.LikeStatusWebSocketHandler;
import com.app.photoservice.utils.UserContext;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PhotoService {

	@Autowired
	private PhotoMetadataRepository photoMetadataRepository;

	@Autowired
	private PhotoStorageService photoStorageService;

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private UserContext userContext;

	@Autowired
	private ReactiveRedisTemplate<String, PhotoDto> reactiveRedisTemplate;

	@Autowired
	private PhotoKafkaProducer kafkaProducer;

	@Autowired
	@Qualifier("cachy")
	private ReactiveRedisTemplate<String, String> cachy;

	@Autowired
	private LikeStatusWebSocketHandler likeStatusWebSocketHandler;

	final static org.slf4j.Logger log = LoggerFactory.getLogger(PhotoService.class);

	public Mono<String> savePhoto(FilePart file) {
		return userContext.getCurrentUserId()
				.flatMap(userId -> DataBufferUtils.join(file.content()).flatMap(dataBuffer -> {
					byte[] fileBytes = new byte[dataBuffer.readableByteCount()];
					dataBuffer.read(fileBytes);
					DataBufferUtils.release(dataBuffer);

					String base64Image = Base64.getEncoder().encodeToString(fileBytes);
					String tempFileId = UUID.randomUUID().toString();
					String cacheKey = "image:" + tempFileId;

					// Salva la stringa Base64 in Redis
					Mono<Boolean> saveInRedis = cachy.opsForValue().set(cacheKey, base64Image)
							.then(cachy.expire(cacheKey, Duration.ofMinutes(10)));

					Mono<Boolean> saveMetadata = saveMetadataToCache(tempFileId, file.filename(),
							extracted(file).toString()

				);

					if (saveMetadata == null) {
						throw new PhotoReadingException("It's not working like that....");
					}

					return saveInRedis.then(saveMetadata).then(Mono.just(tempFileId)).flatMap(tempFileIdResult -> {
						// Pubblica un messaggio Kafka per notificare l'upload
						PhotoEvent event = new PhotoEvent();
						event.setPhotoId(tempFileIdResult);
						event.setUserId(userId.toString());
						event.setTimestamp(Instant.now().toString());

						kafkaProducer.publishPhotoUploadedEvent(event);

						// Aspetta il risultato del topic "photo-processed"
						return Mono.just("Immagine caricata con successo. nella cache ID: " + tempFileIdResult);
					});
				}).onErrorResume(e -> Mono.just("Errore durante il caricamento del file: " + e.getMessage())));
	}

	private MediaType extracted(FilePart file) {
		return file.headers().getContentType();
	}

	public Mono<Boolean> saveMetadataToCache(String photoId, String filename, String contentType) {
		String cacheKey = "metadata:" + photoId;

		Map<String, String> metadata = Map.of("filename", filename, "contentType", contentType);

		return cachy.opsForHash().putAll(cacheKey, metadata).then(cachy.expire(cacheKey, Duration.ofMinutes(10)));
	}

	// Metodo per ottenere una foto per ID
	public Mono<PhotoDto> getPhotoById(Long photoId) {
		String cacheKey = "photo:" + photoId;

		// Controlla se la foto è presente nella cache
		return reactiveRedisTemplate.opsForValue().get(cacheKey).switchIfEmpty(Mono.defer(() -> {
			// Se non è nella cache, recuperala dal database
			Optional<PhotoMetadata> optionalMetadata = photoMetadataRepository.findById(photoId);

			if (optionalMetadata.isEmpty()) {
				return Mono.error(new PhotoNotFoundException("Photo not found"));
			}

			PhotoMetadata metadata = optionalMetadata.get();

			try {
				// Recupera l'immagine da GridFS
				GridFsResource photoResource = photoStorageService.getPhoto(metadata.getFileId());
				byte[] imageBytes = photoResource.getInputStream().readAllBytes();

				// Ottieni il conteggio dei like
				int likeCount = likeRepository.countByPhotoId(photoId);

				// Crea il PhotoDto
				PhotoDto photoDto = PhotoMapper.toPhotoDto(metadata, imageBytes, likeCount);

				// Salva il PhotoDto nella cache
				return reactiveRedisTemplate.opsForValue().set(cacheKey, photoDto)
						.then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(10))).thenReturn(photoDto);

			} catch (IOException e) {
				return Mono.error(new RuntimeException("Errore durante la lettura della foto", e));
			}
		}));
	}

	public Mono<Boolean> addLike(Long photoId) {
		return userContext.getCurrentUserId().flatMap(userId -> {
			Optional<Like> existingLike = likeRepository.findByUserIdAndPhotoId(userId, photoId);
			if (existingLike.isPresent()) {
				return Mono.just(false); // L'utente ha già messo un "like"
			}

			PhotoMetadata photo = photoMetadataRepository.findById(photoId)
					.orElseThrow(() -> new PhotoNotFoundException("Photo not found"));

			Like like = new Like();
			like.setUserId(userId);
			like.setPhoto(photo);
			likeRepository.save(like);

			// Aggiorna il contatore dei like nell'entità PhotoMetadata
			photo.setLikeCount(photo.getLikeCount() + 1);
			photoMetadataRepository.save(photo);

			// Aggiorna il conteggio dei like nella cache
			String cacheKey = "photo:" + photoId;
			return reactiveRedisTemplate.opsForValue().get(cacheKey).flatMap(photoDto -> {
				photoDto.setLikeCount(photoDto.getLikeCount() + 1);
				return reactiveRedisTemplate.opsForValue().set(cacheKey, photoDto)
						.then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(2))).thenReturn(true);
			}).switchIfEmpty(Mono.just(true)) // Se non è nella cache, non fare nulla
					.doOnNext(success -> {
						// Notifica lo stato aggiornato al WebSocket
						likeStatusWebSocketHandler.notifyLikeStatus(photoId, true);
					});
		});
	}

	public Mono<Boolean> removeLike(Long photoId) {
		return userContext.getCurrentUserId().flatMap(userId -> {
			Optional<Like> like = likeRepository.findByUserIdAndPhotoId(userId, photoId);
			if (like.isPresent()) {
				likeRepository.delete(like.get());

				PhotoMetadata photo = photoMetadataRepository.findById(photoId)
						.orElseThrow(() -> new PhotoNotFoundException("Photo not found"));

				// Aggiorna il contatore dei like nell'entità PhotoMetadata
				photo.setLikeCount(photo.getLikeCount() - 1);
				photoMetadataRepository.save(photo);

				// Aggiorna il conteggio dei like nella cache
				String cacheKey = "photo:" + photoId;
				return reactiveRedisTemplate.opsForValue().get(cacheKey).flatMap(photoDto -> {
					photoDto.setLikeCount(photoDto.getLikeCount() - 1);
					return reactiveRedisTemplate.opsForValue().set(cacheKey, photoDto)
							.then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(2))).thenReturn(true);
				}).switchIfEmpty(Mono.just(true)) // Se non è nella cache, non fare nulla
						.doOnNext(success -> {
							// Notifica lo stato aggiornato al WebSocket
							likeStatusWebSocketHandler.notifyLikeStatus(photoId, false);
						});
			}
			return Mono.just(false); // L'utente non ha messo un "like"
		});
	}

	public Mono<Boolean> getLikeStatus(Long photoId) {
		return userContext.getCurrentUserId().flatMap(userId -> {
			if (userId == null) {
				return Mono.just(false); // L'utente non è autenticato
			}
			return Mono.fromCallable(() -> likeRepository.findByUserIdAndPhotoId(userId, photoId))
					.map(Optional::isPresent).defaultIfEmpty(false); // Se non c'è like, restituisci false
		}).onErrorResume(e -> {
			log.error("Errore durante il recupero dello stato del like per photoId {}: {}", photoId, e.getMessage());
			return Mono.just(false);
		});
	}

	// Metodo per ottenere solo i metadati della foto
	public Mono<PhotoDto> getPhotoMetadata(Long photoId) {
		String cacheKey = "photo:" + photoId;

		// Controlla se la foto è presente nella cache
		return reactiveRedisTemplate.opsForValue().get(cacheKey).map(photoDto -> {
			// Rimuovi i bytes dell'immagine per restituire solo i metadati
			photoDto.setImageBytes(null);
			return photoDto;
		}).switchIfEmpty(Mono.defer(() -> {
			// Se non è nella cache, recuperala dal database
			Optional<PhotoMetadata> optionalMetadata = photoMetadataRepository.findById(photoId);

			if (optionalMetadata.isEmpty()) {
				return Mono.error(new PhotoNotFoundException("Photo not found"));
			}

			PhotoMetadata metadata = optionalMetadata.get();

			// Ottieni il conteggio dei like
			int likeCount = likeRepository.countByPhotoId(photoId);

			// Crea il PhotoDto senza i bytes dell'immagine
			PhotoDto photoDto = PhotoMapper.toPhotoDto(metadata, null, likeCount);

			// Salva il PhotoDto nella cache
			return reactiveRedisTemplate.opsForValue().set(cacheKey, photoDto)
					.then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(10))).thenReturn(photoDto);
		}));
	}

	/*
	 * <----------------------------------------------------------------------------
	 * ------------------>
	 */

	public Flux<PhotoDto> getPhotosBatchByUser(int page, int size) {
		return userContext.getCurrentUserId().flatMapMany(userId -> {
			// Recupera i metadati delle foto per l'utente
			Page<PhotoMetadata> photoMetadatas = photoMetadataRepository.findByUserId(userId,
					PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "uploadDate")));

			return Flux.fromIterable(photoMetadatas.getContent()).flatMap(metadata -> {
				String cacheKey = "photo:" + metadata.getId();

				// Verifica se la singola foto è in cache
				return reactiveRedisTemplate.opsForValue().get(cacheKey).switchIfEmpty(Mono.defer(() -> {
					try {
						// Recupera immagine da GridFS
						GridFsResource photoResource = photoStorageService.getPhoto(metadata.getFileId());
						byte[] imageBytes = photoResource.getInputStream().readAllBytes();
						// Ottieni il conteggio dei like
						int likeCount = likeRepository.countByPhotoId(metadata.getId());

						PhotoDto photoDto = PhotoMapper.toPhotoDto(metadata, imageBytes, likeCount);

						// Salva nella cache individuale
						return reactiveRedisTemplate.opsForValue().set(cacheKey, photoDto)
								.then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(5)))
								.thenReturn(photoDto);
					} catch (IOException e) {
						return Mono.error(new RuntimeException("Error fetching photo", e));
					}
				}));
			});
		});
	}
	
	
	public Flux<PhotoDto> getPhotosByUser(Long userId, int page, int size) {
	    return Mono.just(userId).flatMapMany(id -> {
	        // Recupera i metadati delle foto per l'utente specificato
	        Page<PhotoMetadata> photoMetadatas = photoMetadataRepository.findByUserId(id,
	                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "uploadDate")));

	        return Flux.fromIterable(photoMetadatas.getContent()).flatMap(metadata -> {
	            String cacheKey = "photo:" + metadata.getId();

	            // Verifica se la foto è in cache
	            return reactiveRedisTemplate.opsForValue().get(cacheKey).switchIfEmpty(Mono.defer(() -> {
	                try {
	                    // Recupera immagine da GridFS
	                    GridFsResource photoResource = photoStorageService.getPhoto(metadata.getFileId());
	                    byte[] imageBytes = photoResource.getInputStream().readAllBytes();
	                    // Ottieni il conteggio dei like
	                    int likeCount = likeRepository.countByPhotoId(metadata.getId());

	                    PhotoDto photoDto = PhotoMapper.toPhotoDto(metadata, imageBytes, likeCount);

	                    // Salva nella cache
	                    return reactiveRedisTemplate.opsForValue().set(cacheKey, photoDto)
	                            .then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(5)))
	                            .thenReturn(photoDto);
	                } catch (IOException e) {
	                    return Mono.error(new RuntimeException("Error fetching photo", e));
	                }
	            }));
	        });
	    });
	}


	public Flux<PhotoDto> getPhotoMetadataBatch(int page, int size) {
		// Recupera direttamente dal database
		Page<PhotoMetadata> photoMetadatas = photoMetadataRepository
				.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")));

		return Flux.fromIterable(photoMetadatas.getContent()).flatMap(metadata -> {
			String cacheKey = "photo:" + metadata.getId();

			// Verifica se la singola foto è in cache
			return reactiveRedisTemplate.opsForValue().get(cacheKey).switchIfEmpty(Mono.defer(() -> {
				PhotoDto photoDto = PhotoMapper.toPhotoDto(metadata, null, metadata.getLikeCount());

				// Salva nella cache individuale
				return reactiveRedisTemplate.opsForValue().set(cacheKey, photoDto)
						.then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(5))).thenReturn(photoDto);
			}));
		});
	}

	public Flux<PhotoDto> getPhotosBatch(int page, int size) {
		// Recupera direttamente dal database
		Page<PhotoMetadata> photoMetadatas = photoMetadataRepository
				.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")));

		return Flux.fromIterable(photoMetadatas.getContent()).flatMap(metadata -> {
			String cacheKey = "photo:" + metadata.getId();

			// Verifica se la singola foto è in cache
			return reactiveRedisTemplate.opsForValue().get(cacheKey).switchIfEmpty(Mono.defer(() -> {
				try {
					GridFsResource photoResource = photoStorageService.getPhoto(metadata.getFileId());
					byte[] imageBytes = photoResource.getInputStream().readAllBytes();

					PhotoDto photoDto = PhotoMapper.toPhotoDto(metadata, imageBytes, metadata.getLikeCount());

					// Salva nella cache individuale
					return reactiveRedisTemplate.opsForValue().set(cacheKey, photoDto)
							.then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(5))).thenReturn(photoDto);
				} catch (IOException e) {
					return Mono.error(new RuntimeException("Error fetching photo", e));
				}
			}));
		});
	}

	public Flux<PhotoDto> getPhotoMetadataBatchByUser(int page, int size) {
		return userContext.getCurrentUserId().flatMapMany(userId -> {
			// Recupera i metadati delle foto per l'utente
			Page<PhotoMetadata> photoMetadatas = photoMetadataRepository.findByUserId(userId,
					PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "uploadDate")));

			return Flux.fromIterable(photoMetadatas.getContent()).flatMap(metadata -> {
				String cacheKey = "photo:" + metadata.getId();

				// Verifica se la singola foto è in cache
				return reactiveRedisTemplate.opsForValue().get(cacheKey).switchIfEmpty(Mono.defer(() -> {
					PhotoDto photoDto = PhotoMapper.toPhotoDto(metadata, null, metadata.getLikeCount());

					// Salva nella cache individuale
					return reactiveRedisTemplate.opsForValue().set(cacheKey, photoDto)
							.then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(5))).thenReturn(photoDto);
				}));
			});
		});
	}

	public Mono<Boolean> checkLikeStatus(Long photoId) {
		return userContext.getCurrentUserId()
				.flatMap(userId -> Mono.fromCallable(() -> likeRepository.findByUserIdAndPhotoId(userId, photoId))
						.map(optionalLike -> optionalLike.isPresent()).defaultIfEmpty(false).doOnNext(liked -> {
							likeStatusWebSocketHandler.notifyLikeStatus(photoId, liked);
						}));
	}
	
	
	
	

	public Mono<Boolean> deletePhoto(Long photoId) {
	    return userContext.getCurrentUserId().flatMap(userId ->
	        userContext.getUserRole().flatMap(userRole -> {
	            // Recupera i metadati della foto
	            return Mono.fromCallable(() -> photoMetadataRepository.findById(photoId))
	                    .flatMap(optionalMetadata -> {
	                        if (optionalMetadata.isEmpty()) {
	                            throw new PhotoNotFoundException("Foto non trovata."); // Solleviamo un'eccezione
	                        }

	                        PhotoMetadata metadata = optionalMetadata.get();

	                        // Verifica che l'utente sia il proprietario della foto o che sia un amministratore
	                        if (!metadata.getUserId().equals(userId) && !"ADMIN".equals(userRole)) {
	                        	System.out.println(userRole);
	                            throw new SecurityException("Non sei autorizzato a eliminare questa foto.");
	                        }

	                        // Elimina il file da GridFS
	                        photoStorageService.deletePhoto(metadata.getFileId());

	                        // Elimina i metadati dal database
	                        photoMetadataRepository.delete(metadata);

	                        // Elimina la cache associata alla foto
	                        String cacheKey = "photo:" + photoId;
	                        return reactiveRedisTemplate.delete(cacheKey).thenReturn(true);
	                    });
	        })
	    );
	}



}
