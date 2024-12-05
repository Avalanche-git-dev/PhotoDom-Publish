package com.app.photoservice.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.app.photoservice.kafka.event.PhotoEvent;

@Service
public class PhotoKafkaProducer {

    private final KafkaTemplate<String, PhotoEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.photo-uploaded}")
    private String photoUploadedTopic;

    public PhotoKafkaProducer(KafkaTemplate<String, PhotoEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPhotoUploadedEvent(PhotoEvent event) {
        kafkaTemplate.send(photoUploadedTopic, event.getPhotoId(), event);
        System.out.println("Photo uploaded event sent: " + event);
    }
}
