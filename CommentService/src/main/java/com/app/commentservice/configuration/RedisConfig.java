package com.app.commentservice.configuration;



import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisConfig {


    
	

	    @Bean
	    RedisCacheConfiguration cacheConfiguration() {
	        return RedisCacheConfiguration.defaultCacheConfig()
	            .entryTtl(Duration.ofMinutes(2)) 
	            .disableCachingNullValues();   // Evita di memorizzare valori nulli, molto interessante.
	    }

	    @Bean
	     RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
	        return RedisCacheManager.builder(redisConnectionFactory)
	            .cacheDefaults(cacheConfiguration())
	            .build();
	    }
	}

    
