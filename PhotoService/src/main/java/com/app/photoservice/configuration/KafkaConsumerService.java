//package com.app.photoservice.configuration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import com.app.photoservice.repository.PhotoMetadataRepository;
//
//@Service
//public class KafkaConsumerService {
//
//    @Autowired
//    private PhotoMetadataRepository photoRepository;
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @KafkaListener(topics = "user-logged-topic", groupId = "photo-service-group")
//    public void listen(String message) {
//        // Estrai l'ID utente dal messaggio
//        Long userId = extractUserIdFromMessage(message);
//        System.out.println("User logged in with ID: " + userId);
//
//        // Carica le foto ordinate per numero di "mi piace" dal database
//      //  List<PhotoDto> photos = photoRepository.findAllByOrderByLikesDesc();
//
//        // Memorizza la lista di foto in Redis
//      //  redisTemplate.opsForValue().set("photos:top-likes", photos);
//
//        System.out.println("Photos cached in Redis for user ID: " + userId);
//    }
//
//    private Long extractUserIdFromMessage(String message) {
//        return Long.parseLong(message.split(":")[1].trim());
//    }
//}
