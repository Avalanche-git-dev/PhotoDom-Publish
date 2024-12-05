package com.app.userservice.configuration;



import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RedisConfig {
	
	
	
	
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // Configura un ObjectMapper per serializzare gli oggetti complessi
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.activateDefaultTyping(
    	    BasicPolymorphicTypeValidator.builder()
    	        .allowIfSubType("com.app.userservice") // Specifica il package corretto
    	        .build(),
    	    ObjectMapper.DefaultTyping.NON_FINAL,
    	    JsonTypeInfo.As.PROPERTY
    	);
    	objectMapper.findAndRegisterModules();
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Configura un serializzatore generico con Jackson
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Configura RedisCacheManager con TTL e serializzatore
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfig)
                .build();
    }





	
//	 @Bean
//	    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//	        // Configura un PolymorphicTypeValidator per il supporto del polimorfismo
//	        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
//	                .allowIfSubType("com.app.userservice.entity")
//	                .allowIfSubType("com.app.userservice")
//	                .allowIfSubType("java.util")
//	                .allowIfSubType("java.lang")
//	                .allowIfSubType("java.lang.Object")// Permetti tipi standard come ArrayList
//	                .build();
//// Permetti la serializzazione solo per il package specificato
//
//	        // Configura l'ObjectMapper per includere il tipo '@class'
//	        ObjectMapper objectMapper = new ObjectMapper();
//	        objectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//	        objectMapper.findAndRegisterModules(); // Per supportare tipi come LocalDate
//	        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Usa formati leggibili ISO-8601 per date
//
//	        // Configura il serializzatore con l'ObjectMapper personalizzato
//	        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//	        // Configura il RedisCacheManager con TTL e serializzatore
//	        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//	                .entryTtl(Duration.ofMinutes(10)) // Durata della cache
//	                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
//
//	        return RedisCacheManager.builder(redisConnectionFactory)
//	                .cacheDefaults(cacheConfig)
//	                .build();
//	    }
}
