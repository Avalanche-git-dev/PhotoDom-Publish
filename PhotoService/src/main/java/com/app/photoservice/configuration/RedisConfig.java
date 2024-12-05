package com.app.photoservice.configuration;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.app.photoservice.dto.PhotoDto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

@Configuration
public class RedisConfig {

     @Bean
     ReactiveRedisTemplate<String, PhotoDto> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        // Configura il serializzatore per le chiavi come Stringhe
        RedisSerializationContext.RedisSerializationContextBuilder<String, PhotoDto> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        // Configura l'ObjectMapper per serializzare/deserializzare PhotoDto
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Registra i moduli per gestire tipi come LocalDate
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        // Configura il serializzatore per i valori come JSON
        Jackson2JsonRedisSerializer<PhotoDto> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,PhotoDto.class);

        RedisSerializationContext<String, PhotoDto> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
     
     
     
     
     @Bean("cachy")
     ReactiveRedisTemplate<String, String> reactiveRedisTemplateTemp(ReactiveRedisConnectionFactory connectionFactory) {
         // Configura il serializzatore StringRedisSerializer per chiavi e valori
         StringRedisSerializer stringSerializer = new StringRedisSerializer();

         // Crea il contesto di serializzazione
         RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
                 .<String, String>newSerializationContext(stringSerializer)
                 .key(stringSerializer)   // Serializza le chiavi come stringhe
                 .value(stringSerializer) // Serializza i valori come stringhe
                 .hashKey(stringSerializer)
                 .hashValue(stringSerializer)
                 .build();

         // Crea e restituisci il ReactiveRedisTemplate
         return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
     }
    
    
    
    
    
    
    
}
