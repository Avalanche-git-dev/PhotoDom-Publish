//package com.app.userservice.configuration;
//
//import java.time.Duration;
//
////import java.time.Duration;
//
////import com.fasterxml.jackson.annotation.JsonTypeInfo;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Configuration
//public class RedisConfig {
//	
//	
//	private final ObjectMapper objectMapper;
//	
//	
//	 public RedisConfig(ObjectMapper objectMapper) {
//	        this.objectMapper = objectMapper;
//	    }
//
//	@Bean
//	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//		RedisTemplate<String, Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(redisConnectionFactory);
//
//		// Set key serializer
//		template.setKeySerializer(new StringRedisSerializer());
//		// Set value serializer
//		template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//
//		return template;
//	}
//
//	@Bean
//	RedisCacheConfiguration cacheConfiguration() {
//		return RedisCacheConfiguration.defaultCacheConfig()
//				.entryTtl(Duration.ofMinutes(5))
//				.serializeKeysWith(
//						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//				.serializeValuesWith(RedisSerializationContext.SerializationPair
//						.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
//	}
//
//
//
//
//@Bean
//public RedisCacheManager cacheManger(RedisConnectionFactory redisConnectionFactory) {
//	 RedisCacheConfiguration cacheConfig = cacheConfiguration();
//	  return RedisCacheManager.builder(redisConnectionFactory)
//              .cacheDefaults(cacheConfig)
//              .build();
//}
//
//
//
//
//
//
//} 
//
//
////	 @Bean
////	    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
////	        // Configura un PolymorphicTypeValidator per il supporto del polimorfismo
////	        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
////	                .allowIfSubType("com.app.userservice.entity")
////	                .allowIfSubType("com.app.userservice")
////	                .allowIfSubType("java.util")
////	                .allowIfSubType("java.lang")
////	                .allowIfSubType("java.lang.Object")// Permetti tipi standard come ArrayList
////	                .build();
////// Permetti la serializzazione solo per il package specificato
////
////	        // Configura l'ObjectMapper per includere il tipo '@class'
////	        ObjectMapper objectMapper = new ObjectMapper();
////	        objectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
////	        objectMapper.findAndRegisterModules(); // Per supportare tipi come LocalDate
////	        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Usa formati leggibili ISO-8601 per date
////
////	        // Configura il serializzatore con l'ObjectMapper personalizzato
////	        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
////
////	        // Configura il RedisCacheManager con TTL e serializzatore
////	        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
////	                .entryTtl(Duration.ofMinutes(10)) // Durata della cache
////	                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
////
////	        return RedisCacheManager.builder(redisConnectionFactory)
////	                .cacheDefaults(cacheConfig)
////	                .build();
////	    }




package com.app.userservice.configuration;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RedisConfig {

   
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // Registra moduli per Java 8 e supporto di LocalDate
//        objectMapper.registerModule(new Jdk8Module());
//        objectMapper.registerModule(new JavaTimeModule());
//
//        // Configura formati specifici per JSON (già gestito da annotazioni in UserDto)
//        objectMapper.findAndRegisterModules();
//
//        return objectMapper;
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//
//        // Configura i serializzatori
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
//
//        return template;
//    }
//
//    @Bean
//    public RedisCacheConfiguration cacheConfiguration(ObjectMapper objectMapper) {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10)) // TTL di 10 minuti
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
//    }
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, RedisCacheConfiguration cacheConfiguration) {
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cacheConfiguration)
//                .build();
//    }
}

