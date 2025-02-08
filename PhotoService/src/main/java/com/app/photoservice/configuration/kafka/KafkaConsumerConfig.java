package com.app.photoservice.configuration.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import com.app.photoservice.kafka.event.PhotoProcessedEvent;

@Configuration
public class KafkaConsumerConfig {


@Bean
public ConsumerFactory<String, PhotoProcessedEvent> consumerFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Modifica con il tuo server Kafka
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "photo-service-group");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
    config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.app.photoservice.kafka.event.PhotoProcessedEvent");
    config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

    return new DefaultKafkaConsumerFactory<>(config);
}

@Bean
public ConcurrentKafkaListenerContainerFactory<String, PhotoProcessedEvent> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PhotoProcessedEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(1000L, 2))); // Gestione errori
    return factory;
}



}