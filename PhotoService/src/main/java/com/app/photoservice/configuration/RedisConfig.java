package com.app.photoservice.configuration;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.app.photoservice.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

@Configuration
public class RedisConfig {

    @Bean
     ReactiveRedisTemplate<String, CommentDto> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        // Configura il serializzatore per le chiavi come Stringhe
        RedisSerializationContext.RedisSerializationContextBuilder<String, CommentDto> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        // Configura l'ObjectMapper per serializzare/deserializzare PhotoDto
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Registra i moduli per gestire tipi come LocalDate
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        // Configura il serializzatore per i valori come JSON
        Jackson2JsonRedisSerializer<CommentDto> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,CommentDto.class);

        RedisSerializationContext<String, CommentDto> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
    
    
    
    
    
    
    
}
