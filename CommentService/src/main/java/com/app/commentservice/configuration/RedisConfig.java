package com.app.commentservice.configuration;



import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisConfig {

//    @Bean
//     ReactiveRedisTemplate<String, CommentDto> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
//        // Configura il serializzatore per le chiavi come Stringhe
//        RedisSerializationContext.RedisSerializationContextBuilder<String, CommentDto> builder =
//                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
//
//        // Configura l'ObjectMapper per serializzare/deserializzare PhotoDto
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules(); // Registra i moduli per gestire tipi come LocalDate
//        objectMapper.activateDefaultTyping(
//                LaissezFaireSubTypeValidator.instance,
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY);
//
//        // Configura il serializzatore per i valori come JSON
//        Jackson2JsonRedisSerializer<CommentDto> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,CommentDto.class);
//
//        RedisSerializationContext<String, CommentDto> context = builder.value(serializer).build();
//
//        return new ReactiveRedisTemplate<>(connectionFactory, context);
//    }
//    
//    
//    
    
    
    
	

	    @Bean
	    RedisCacheConfiguration cacheConfiguration() {
	        return RedisCacheConfiguration.defaultCacheConfig()
	            .entryTtl(Duration.ofHours(1)) // Durata della cache
	            .disableCachingNullValues();   // Evita di memorizzare valori nulli
	    }

	    @Bean
	     RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
	        return RedisCacheManager.builder(redisConnectionFactory)
	            .cacheDefaults(cacheConfiguration())
	            .build();
	    }
	}

    
