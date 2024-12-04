package com.app.photoservice.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.app.photoservice.dto.CommentDto;
import com.app.photoservice.dto.PhotoMapper;
import com.app.photoservice.entity.Like;
import com.app.photoservice.entity.PhotoMetadata;
import com.app.photoservice.exception.PhotoNotFoundException;
import com.app.photoservice.repository.LikeRepository;
import com.app.photoservice.repository.PhotoMetadataRepository;
import com.app.photoservice.utils.UserContext;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
    private ReactiveRedisTemplate<String, CommentDto> reactiveRedisTemplate;

    // Metodo per salvare una foto
    public Mono<CommentDto> savePhoto(FilePart file) {
        return userContext.getCurrentUserId()
                .flatMap(userId ->
                        file.content()
                                .map(dataBuffer -> dataBuffer.asInputStream(true))
                                .next()
                                .flatMap(inputStream -> {
                                    try {
                                        // Leggi i byte dell'immagine
                                        byte[] imageBytes = inputStream.readAllBytes();

                                        // Calcola la dimensione del file
                                        long size = imageBytes.length;

                                        // Salva la foto in MongoDB GridFS e ottieni l'ID del file
                                        String fileId = photoStorageService.savePhoto(
                                                new ByteArrayInputStream(imageBytes),
                                                file.filename(),
                                                file.headers().getContentType().toString()
                                        );

                                        // Crea e salva i metadati della foto
                                        PhotoMetadata metadata = new PhotoMetadata();
                                        metadata.setUserId(userId);
                                        metadata.setFilename(file.filename());
                                        metadata.setContentType(file.headers().getContentType().toString());
                                        metadata.setSize(size);
                                        metadata.setFileId(fileId);
                                        metadata.setUploadDate(new Date());

                                        PhotoMetadata savedMetadata = photoMetadataRepository.save(metadata);

                                        // Crea il PhotoDto
                                        CommentDto photoDto = PhotoMapper.toPhotoDto(savedMetadata, imageBytes, 0);

                                        // Salva il PhotoDto nella cache
                                        String cacheKey = "photo:" + savedMetadata.getId();
                                        return reactiveRedisTemplate.opsForValue()
                                                .set(cacheKey, photoDto)
                                                .then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(10)))
                                                .thenReturn(photoDto);

                                    } catch (IOException e) {
                                        return Mono.error(new RuntimeException("Errore durante il salvataggio del file", e));
                                    }
                                })
                );
    }

    // Metodo per ottenere una foto per ID
    public Mono<CommentDto> getPhotoById(Long photoId) {
        String cacheKey = "photo:" + photoId;

        // Controlla se la foto è presente nella cache
        return reactiveRedisTemplate.opsForValue().get(cacheKey)
                .switchIfEmpty(Mono.defer(() -> {
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
                        CommentDto photoDto = PhotoMapper.toPhotoDto(metadata, imageBytes, likeCount);

                        // Salva il PhotoDto nella cache
                        return reactiveRedisTemplate.opsForValue()
                                .set(cacheKey, photoDto)
                                .then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(10)))
                                .thenReturn(photoDto);

                    } catch (IOException e) {
                        return Mono.error(new RuntimeException("Errore durante la lettura della foto", e));
                    }
                }));
    }

//    // Metodo per aggiungere un like
//    public Mono<Boolean> addLike(Long photoId) {
//        return userContext.getCurrentUserId()
//                .flatMap(userId -> {
//                    Optional<Like> existingLike = likeRepository.findByUserIdAndPhotoId(userId, photoId);
//                    if (existingLike.isPresent()) {
//                        return Mono.just(false); // L'utente ha già messo un "like"
//                    }
//
//                    PhotoMetadata photo = photoMetadataRepository.findById(photoId)
//                            .orElseThrow(() -> new PhotoNotFoundException("Photo not found"));
//
//                    Like like = new Like();
//                    like.setUserId(userId);
//                    like.setPhoto(photo);
//                    likeRepository.save(like);
//
//                    // Aggiorna il conteggio dei like nella cache
//                    String cacheKey = "photo:" + photoId;
//                    return reactiveRedisTemplate.opsForValue().get(cacheKey)
//                            .flatMap(photoDto -> {
//                                photoDto.setLikeCount(photoDto.getLikeCount() + 1);
//                                return reactiveRedisTemplate.opsForValue()
//                                        .set(cacheKey, photoDto)
//                                        .thenReturn(true);
//                            })
//                            .switchIfEmpty(Mono.just(true)); // Se non è nella cache, non fare nulla
//                });
//    }
//
//    // Metodo per rimuovere un like
//    public Mono<Boolean> removeLike(Long photoId) {
//        return userContext.getCurrentUserId()
//                .flatMap(userId -> {
//                    Optional<Like> like = likeRepository.findByUserIdAndPhotoId(userId, photoId);
//                    if (like.isPresent()) {
//                        likeRepository.delete(like.get());
//
//                        // Aggiorna il conteggio dei like nella cache
//                        String cacheKey = "photo:" + photoId;
//                        return reactiveRedisTemplate.opsForValue().get(cacheKey)
//                                .flatMap(photoDto -> {
//                                    photoDto.setLikeCount(photoDto.getLikeCount() - 1);
//                                    return reactiveRedisTemplate.opsForValue()
//                                            .set(cacheKey, photoDto)
//                                            .thenReturn(true);
//                                })
//                                .switchIfEmpty(Mono.just(true)); // Se non è nella cache, non fare nulla
//                    }
//                    return Mono.just(false); // L'utente non ha messo un "like"
//                });
//    }
    public Mono<Boolean> addLike(Long photoId) {
        return userContext.getCurrentUserId()
                .flatMap(userId -> {
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
                    return reactiveRedisTemplate.opsForValue().get(cacheKey)
                            .flatMap(photoDto -> {
                                photoDto.setLikeCount(photoDto.getLikeCount() + 1);
                                return reactiveRedisTemplate.opsForValue()
                                        .set(cacheKey, photoDto)
                                        .thenReturn(true);
                            })
                            .switchIfEmpty(Mono.just(true)); // Se non è nella cache, non fare nulla
                });
    }

    
    public Mono<Boolean> removeLike(Long photoId) {
        return userContext.getCurrentUserId()
                .flatMap(userId -> {
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
                        return reactiveRedisTemplate.opsForValue().get(cacheKey)
                                .flatMap(photoDto -> {
                                    photoDto.setLikeCount(photoDto.getLikeCount() - 1);
                                    return reactiveRedisTemplate.opsForValue()
                                            .set(cacheKey, photoDto)
                                            .thenReturn(true);
                                })
                                .switchIfEmpty(Mono.just(true)); // Se non è nella cache, non fare nulla
                    }
                    return Mono.just(false); // L'utente non ha messo un "like"
                });
    }

    
    
    
    
    
    
    // Metodo per ottenere solo i metadati della foto
    public Mono<CommentDto> getPhotoMetadata(Long photoId) {
        String cacheKey = "photo:" + photoId;

        // Controlla se la foto è presente nella cache
        return reactiveRedisTemplate.opsForValue().get(cacheKey)
                .map(photoDto -> {
                    // Rimuovi i bytes dell'immagine per restituire solo i metadati
                    photoDto.setImageBytes(null);
                    return photoDto;
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Se non è nella cache, recuperala dal database
                    Optional<PhotoMetadata> optionalMetadata = photoMetadataRepository.findById(photoId);

                    if (optionalMetadata.isEmpty()) {
                        return Mono.error(new PhotoNotFoundException("Photo not found"));
                    }

                    PhotoMetadata metadata = optionalMetadata.get();

                    // Ottieni il conteggio dei like
                    int likeCount = likeRepository.countByPhotoId(photoId);

                    // Crea il PhotoDto senza i bytes dell'immagine
                    CommentDto photoDto = PhotoMapper.toPhotoDto(metadata, null, likeCount);

                    // Salva il PhotoDto nella cache
                    return reactiveRedisTemplate.opsForValue()
                            .set(cacheKey, photoDto)
                            .then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(10)))
                            .thenReturn(photoDto);
                }));
    }
    
///Batching for front end
    
    public Flux<CommentDto> getPhotoMetadataBatch(int page, int size) {
        String cacheKey = "photos:metadata:page:" + page;

        return reactiveRedisTemplate.opsForList().range(cacheKey, 0, -1)
                .switchIfEmpty(Flux.defer(() -> {
                    // Carica dal database se non presente nella cache
                    Page<PhotoMetadata> photoMetadatas = photoMetadataRepository.findAll(
                            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")));

                    List<CommentDto> photoDtos = photoMetadatas.stream().map(metadata -> {
                        // Il likeCount è già presente in metadata
                        return PhotoMapper.toPhotoDto(metadata, null, metadata.getLikeCount());
                    }).collect(Collectors.toList());

                    // Cache il batch di metadati
                    return reactiveRedisTemplate.opsForList().rightPushAll(cacheKey, photoDtos)
                            .then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(10)))
                            .thenMany(Flux.fromIterable(photoDtos));
                }));
    }

    
    
    public Flux<CommentDto> getPhotosBatch(int page, int size) {
        String cacheKey = "photos:full:page:" + page;

        return reactiveRedisTemplate.opsForList().range(cacheKey, 0, -1)
                .switchIfEmpty(Flux.defer(() -> {
                    Page<PhotoMetadata> photoMetadatas = photoMetadataRepository.findAll(
                            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")));

                    return Flux.fromIterable(photoMetadatas)
                            .flatMap(metadata -> {
                                return Mono.fromCallable(() -> {
                                    GridFsResource photoResource = photoStorageService.getPhoto(metadata.getFileId());
                                    byte[] imageBytes = photoResource.getInputStream().readAllBytes();

                                    CommentDto photoDto = PhotoMapper.toPhotoDto(metadata, imageBytes, metadata.getLikeCount());

                                    // Cache il singolo PhotoDto
                                    String photoCacheKey = "photo:" + metadata.getId();
                                    reactiveRedisTemplate.opsForValue().set(photoCacheKey, photoDto).subscribe();

                                    return photoDto;
                                });
                            })
                            .collectList()
                            .flatMapMany(photoDtos -> {
                                // Cache il batch di foto
                                reactiveRedisTemplate.opsForList().rightPushAll(cacheKey, photoDtos).subscribe();
                                reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(10)).subscribe();

                                return Flux.fromIterable(photoDtos);
                            });
                }));
    }
    
    
    
    public Flux<CommentDto> getPhotoMetadataBatchByUser(int page, int size) {
        return userContext.getCurrentUserId()
                .flatMapMany(userId -> {
                    String cacheKey = "user:" + userId + ":photos:metadata:page:" + page;

                    return reactiveRedisTemplate.opsForList().range(cacheKey, 0, -1)
                            .switchIfEmpty(
                                    Mono.fromCallable(() -> {
                                        // Chiamata bloccante al repository
                                        Page<PhotoMetadata> photoMetadatas = photoMetadataRepository.findByUserId(
                                                userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "uploadDate")));
                                        return photoMetadatas.getContent();
                                    })
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .flatMapMany(photoMetadataList -> {
                                        List<CommentDto> photoDtos = photoMetadataList.stream()
                                                .map(metadata -> {
                                                    // Usa direttamente metadata.getLikeCount()
                                                    return PhotoMapper.toPhotoDto(metadata, null, metadata.getLikeCount());
                                                })
                                                .collect(Collectors.toList());

                                        // Salva il batch di metadati nella cache
                                        return reactiveRedisTemplate.opsForList()
                                                .rightPushAll(cacheKey, photoDtos)
                                                .then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(10)))
                                                .thenMany(Flux.fromIterable(photoDtos));
                                    })
                            );
                });
    }

    
    
    
    public Flux<CommentDto> getPhotosBatchByUser(int page, int size) {
        return userContext.getCurrentUserId()
                .flatMapMany(userId -> {
                    String cacheKey = "user:" + userId + ":photos:full:page:" + page;

                    return reactiveRedisTemplate.opsForList().range(cacheKey, 0, -1)
                            .switchIfEmpty(
                                    Mono.fromCallable(() -> {
                                        // Chiamata bloccante al repository
                                        Page<PhotoMetadata> photoMetadatas = photoMetadataRepository.findByUserId(
                                                userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "uploadDate")));
                                        return photoMetadatas.getContent();
                                    })
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .flatMapMany(photoMetadataList ->
                                            Flux.fromIterable(photoMetadataList)
                                                    .flatMap(metadata ->
                                                            Mono.fromCallable(() -> {
                                                                // Chiamata bloccante per ottenere l'immagine
                                                                GridFsResource photoResource = photoStorageService.getPhoto(metadata.getFileId());
                                                                byte[] imageBytes = photoResource.getInputStream().readAllBytes();

                                                                CommentDto photoDto = PhotoMapper.toPhotoDto(metadata, imageBytes, metadata.getLikeCount());
                                                                return photoDto;
                                                            })
                                                            .subscribeOn(Schedulers.boundedElastic())
                                                            .flatMap(photoDto -> {
                                                                // Salva il singolo PhotoDto nella cache
                                                                String photoCacheKey = "photo:" + metadata.getId();
                                                                return reactiveRedisTemplate.opsForValue()
                                                                        .set(photoCacheKey, photoDto)
                                                                        .thenReturn(photoDto);
                                                            })
                                                    )
                                                    .collectList()
                                                    .flatMapMany(photoDtos -> {
                                                        // Salva il batch di foto nella cache
                                                        return reactiveRedisTemplate.opsForList()
                                                                .rightPushAll(cacheKey, photoDtos)
                                                                .then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(10)))
                                                                .thenMany(Flux.fromIterable(photoDtos));
                                                    })
                                    )
                            );
                });
    }






}

