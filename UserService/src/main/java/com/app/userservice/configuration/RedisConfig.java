package com.app.userservice.configuration;



import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration
public class RedisConfig {

//    @Bean
//    RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }
//
//    @Bean
//     RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }
	
	
	 @Bean
	    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
	        // Configura un PolymorphicTypeValidator per il supporto del polimorfismo
	        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
	                .allowIfSubType("com.app.userservice.entity")
	                .allowIfSubType("java.util")
	                .allowIfSubType("java.lang")// Permetti tipi standard come ArrayList
	                .build();
// Permetti la serializzazione solo per il package specificato

	        // Configura l'ObjectMapper per includere il tipo '@class'
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
	        objectMapper.findAndRegisterModules(); // Per supportare tipi come LocalDate
	        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Usa formati leggibili ISO-8601 per date

	        // Configura il serializzatore con l'ObjectMapper personalizzato
	        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

	        // Configura il RedisCacheManager con TTL e serializzatore
	        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
	                .entryTtl(Duration.ofMinutes(10)) // Durata della cache
	                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

	        return RedisCacheManager.builder(redisConnectionFactory)
	                .cacheDefaults(cacheConfig)
	                .build();
	    }
}
