package com.app.photoservice.kafka;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.app.photoservice.dto.PhotoDto;
import com.app.photoservice.dto.PhotoMapper;
import com.app.photoservice.entity.PhotoMetadata;
import com.app.photoservice.kafka.event.PhotoProcessedEvent;
import com.app.photoservice.repository.PhotoMetadataRepository;
import com.app.photoservice.service.PhotoStorageService;
import com.app.photoservice.socket.PhotoWebSocketHandler;

import reactor.core.publisher.Mono;

@Service
public class PhotoProcessedConsumer {

	private final PhotoMetadataRepository photoMetadataRepository;
	private final PhotoStorageService photoStorageService;
	private final ReactiveRedisTemplate<String, String> cachy;
	private final ReactiveRedisTemplate<String, PhotoDto> photoCache;
	private final PhotoWebSocketHandler photoWebSocketHandler;

	public PhotoProcessedConsumer(PhotoMetadataRepository photoMetadataRepository,
			PhotoStorageService photoStorageService, @Qualifier("cachy") ReactiveRedisTemplate<String, String> cachy,
			ReactiveRedisTemplate<String, PhotoDto> photoCache, PhotoWebSocketHandler photoWebSocketHandler) {
		this.photoMetadataRepository = photoMetadataRepository;
		this.photoStorageService = photoStorageService;
		this.cachy = cachy;
		this.photoCache = photoCache;
		this.photoWebSocketHandler = photoWebSocketHandler;
	}

	@KafkaListener(topics = "photo-processed", groupId = "photo-service-group", containerFactory = "kafkaListenerContainerFactory")
	public void consumePhotoProcessedEvent(PhotoProcessedEvent event) {
		try {
			String photoId = event.getPhotoId();
			String resultKey = "result:photo:" + photoId;
			String metadataKey = "metadata:" + photoId;

			if ("error".equals(event.getStatus())) {
				System.err.println("Errore ricevuto dal servizio: " + event.getReason());
				return;
			}

			// Recupera immagine processata e metadati dalla cache
			Mono<String> imageMono = cachy.opsForValue().get(resultKey);
			Mono<Map<Object, Object>> metadataMono = cachy.opsForHash().entries(metadataKey)
					.collectMap(Map.Entry::getKey, Map.Entry::getValue);

			Mono.zip(imageMono, metadataMono).flatMap(tuple -> {
				String base64Image = tuple.getT1();
				Map<Object, Object> metadataMap = tuple.getT2();

				if (base64Image == null || metadataMap.isEmpty()) {
					return Mono.error(new RuntimeException("Dati mancanti in Redis per ID: " + photoId));
				}

				// Conversione dei metadati
				String filename = (String) metadataMap.get("filename");
				String contentType = (String) metadataMap.get("contentType");

				if (filename == null || contentType == null) {
					return Mono.error(new RuntimeException("Metadati incompleti in Redis per ID: " + photoId));
				}

				// Decodifica immagine
				byte[] imageBytes = Base64.getDecoder().decode(base64Image);

				// Salva immagine e metadati nel database
				String fileId = photoStorageService.savePhoto(new ByteArrayInputStream(imageBytes), filename,
						contentType);

				PhotoMetadata metadata = new PhotoMetadata();
				metadata.setUserId(Long.valueOf(event.getUserId()));
				metadata.setFilename(filename);
				metadata.setContentType(contentType);
				metadata.setSize((long) imageBytes.length);
				metadata.setFileId(fileId);
				metadata.setUploadDate(new Date());

				PhotoMetadata savedMetadata = photoMetadataRepository.save(metadata);
				photoWebSocketHandler.notifyNewPhoto(savedMetadata.getId().toString());

				// System.out.println(savedMetadata.toString());

				// Crea oggetto PhotoDto e aggiorna la cache
				PhotoDto photoDto = PhotoMapper.toPhotoDto(savedMetadata, imageBytes, 0);
				String photoCacheKey = "photo:" + savedMetadata.getId();

				return photoCache.opsForValue().set(photoCacheKey, photoDto)
						.then(photoCache.expire(photoCacheKey, Duration.ofMinutes(10))).then(Mono.just(savedMetadata));
			}).flatMap(savedMetadata -> {
				// Rimuove chiavi temporanee dalla cache
				return cachy.delete("result:photo:" + photoId).then(cachy.delete("metadata:" + photoId))
						.then(cachy.delete("image:" + photoId))
						.thenReturn("Foto salvata e cache aggiornata per ID: " + savedMetadata.getId());
			})

					.subscribe(successMessage -> System.out.println(successMessage), error -> System.err
							.println("Errore durante il processamento della foto: " + error.getMessage()));

		} catch (Exception e) {
			System.err.println("Errore critico durante l'elaborazione del messaggio Kafka: " + e.getMessage());
		}
	}
}
