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
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

//@Service
//public class PhotoProcessedConsumer {
//
//    private final PhotoMetadataRepository photoMetadataRepository;
//    private final PhotoStorageService photoStorageService;
//    private final ReactiveRedisTemplate<String, String> cachy;
//    private final ReactiveRedisTemplate<String, PhotoDto> photoCache;
//
//    public PhotoProcessedConsumer(
//            PhotoMetadataRepository photoMetadataRepository,
//            PhotoStorageService photoStorageService,
//            @Qualifier("cachy") ReactiveRedisTemplate<String, String> cachy,
//            ReactiveRedisTemplate<String, PhotoDto> photoCache) {
//        this.photoMetadataRepository = photoMetadataRepository;
//        this.photoStorageService = photoStorageService;
//        this.cachy = cachy;
//        this.photoCache = photoCache;
//    }
//
//    @KafkaListener(topics = "photo-processed", groupId = "photo-service-group",containerFactory = "kafkaListenerContainerFactory")
//    public void consumePhotoProcessedEvent(PhotoProcessedEvent message) {
//        try {
//            // 1. Deserializza il messaggio ricevuto
////            ObjectMapper objectMapper = new ObjectMapper();
////            PhotoProcessedEvent message = objectMapper.readValue(message, PhotoProcessedEvent.class);
//
//            String photoId = message.getPhotoId();
//            String cacheKey = "result:photo:" + photoId;
//
//            if ("error".equals(message.getStatus())) {
//                // Notifica l'errore e termina
//                System.err.println("Errore ricevuto dal servizio: " + message.getReason());
//                return;
//            }
//
//            // 2. Recupera i dati dalla cache
//            cachy.opsForHash().entries(cacheKey)
//                .collectMap(entry -> entry.getKey().toString(), entry -> entry.getValue().toString())
//                .flatMap(metadata -> processAndSavePhoto(metadata, message))
//                .subscribe(
//                    successMessage -> System.out.println(successMessage),
//                    error -> System.err.println("Errore durante il salvataggio della foto: " + error.getMessage())
//                );
//
//        } catch (Exception e) {
//            System.err.println("Errore critico durante l'elaborazione del messaggio Kafka: " + e.getMessage());
//        }
//    }
//
//    private Mono<String> processAndSavePhoto(Map<String, String> metadata, PhotoProcessedEvent event) {
//        try {
//            String photoId = event.getPhotoId();
//            String userId = event.getUserId();
//
//            // 1. Estrai i metadati
//            String base64Image = metadata.get("base64");
//            String filename = metadata.get("filename");
//            String contentType = metadata.get("contentType");
//
//            if (base64Image == null) {
//                return Mono.error(new RuntimeException("Immagine processata non trovata in Redis per ID: " + photoId));
//            }
//
//            // 2. Decodifica l'immagine
//            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
//
//            // 3. Salva l'immagine nel database (GridFS)
//            String fileId = photoStorageService.savePhoto(new ByteArrayInputStream(imageBytes), filename, contentType);
//
//            // 4. Crea e salva i metadati nel database
//            PhotoMetadata metadataObj = new PhotoMetadata();
//            metadataObj.setUserId(Long.valueOf(userId));
//            metadataObj.setFilename(filename);
//            metadataObj.setContentType(contentType);
//            metadataObj.setSize((long) imageBytes.length);
//            metadataObj.setFileId(fileId);
//            metadataObj.setUploadDate(new Date());
//
//            PhotoMetadata savedMetadata = photoMetadataRepository.save(metadataObj);
//
//            // 5. Aggiorna la cache con il PhotoDto
//            PhotoDto photoDto = PhotoMapper.toPhotoDto(savedMetadata, imageBytes, 0);
//            String photoCacheKey = "photo:" + savedMetadata.getId();
//
//            return photoCache.opsForValue()
//                .set(photoCacheKey, photoDto)
//                .then(photoCache.expire(photoCacheKey, Duration.ofDays(1)))
//                .thenReturn("Immagine processata e salvata con successo: " + savedMetadata.getId());
//
//        } catch (Exception e) {
//            return Mono.error(new RuntimeException("Errore durante il processamento della foto: " + e.getMessage()));
//        }
//    }
//}






    
    
    
    
@Service
public class PhotoProcessedConsumer {

    private final PhotoMetadataRepository photoMetadataRepository;
    private final PhotoStorageService photoStorageService;
    private final ReactiveRedisTemplate<String, String> cachy;
    private final ReactiveRedisTemplate<String, PhotoDto> photoCache;

    public PhotoProcessedConsumer(
            PhotoMetadataRepository photoMetadataRepository,
            PhotoStorageService photoStorageService,
            @Qualifier("cachy") ReactiveRedisTemplate<String, String> cachy,
            ReactiveRedisTemplate<String, PhotoDto> photoCache) {
        this.photoMetadataRepository = photoMetadataRepository;
        this.photoStorageService = photoStorageService;
        this.cachy = cachy;
        this.photoCache = photoCache;
    }

    @KafkaListener(topics = "photo-processed", groupId = "photo-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumePhotoProcessedEvent(PhotoProcessedEvent event) {
        try {
            String photoId = event.getPhotoId();
            String resultKey = "result:photo:" + photoId;
            String metadataKey = "metadata:" + photoId;
//
            if ("error".equals(event.getStatus())) {
                System.err.println("Errore ricevuto dal servizio: " + event.getReason());
                return;
            }
            
            
            
//            Mono.just(event)
//            .flatMap(evt -> {
//                if ("error".equals(evt.getStatus())) {
//                    // Genera un errore se lo stato è "error"
//                    return Mono.error(new RuntimeException("Errore ricevuto dal servizio: " + evt.getReason()));
//                }
//                return Mono.just(evt);
//            });

            // Recupera immagine processata e metadati dalla cache
            Mono<String> imageMono = cachy.opsForValue().get(resultKey);
            Mono<Map<Object, Object>> metadataMono = cachy.opsForHash()
            	    .entries(metadataKey)
            	    .collectMap(Map.Entry::getKey, Map.Entry::getValue);

            Mono.zip(imageMono, metadataMono)
                .flatMap(tuple -> {
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
                    String fileId = photoStorageService.savePhoto(new ByteArrayInputStream(imageBytes), filename, contentType);

                    PhotoMetadata metadata = new PhotoMetadata();
                    metadata.setUserId(Long.valueOf(event.getUserId()));
                    metadata.setFilename(filename);
                    metadata.setContentType(contentType);
                    metadata.setSize((long) imageBytes.length);
                    metadata.setFileId(fileId);
                    metadata.setUploadDate(new Date());
                    
                    

                    PhotoMetadata savedMetadata = photoMetadataRepository.save(metadata);
                    
                    
//                    try {
                       // PhotoMetadata exampleMetadata = photoMetadataRepository.save(metadata);
                        System.out.println(savedMetadata.toString());
//                    } catch (Exception e) {
//                        System.err.println("Errore durante il salvataggio dei metadati: " + e.getMessage());
//                      
//                    }

                    // Crea oggetto PhotoDto e aggiorna la cache
                    PhotoDto photoDto = PhotoMapper.toPhotoDto(savedMetadata, imageBytes, 0);
                    String photoCacheKey = "photo:" + savedMetadata.getId();

                    return photoCache.opsForValue()
                        .set(photoCacheKey, photoDto)
                        .then(photoCache.expire(photoCacheKey, Duration.ofDays(1)))
                        .then(Mono.just(savedMetadata));
                })
                .flatMap(savedMetadata -> {
                    // Rimuove chiavi temporanee dalla cache
                    return cachy.delete("result:photo:" + photoId)
                        .then(cachy.delete("metadata:" + photoId))
                        .then(cachy.delete("image:" + photoId))
                        .thenReturn("Foto salvata e cache aggiornata per ID: " + savedMetadata.getId());
                })
                

                .subscribe(
                    successMessage -> System.out.println(successMessage),
                    error -> System.err.println("Errore durante il processamento della foto: " + error.getMessage())
                );

        } catch (Exception e) {
            System.err.println("Errore critico durante l'elaborazione del messaggio Kafka: " + e.getMessage());
        }
    }
}





