package com.api.gateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfiguration {
	
	@Bean
	RouteLocator getCommentRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("comment-service", r -> r.path("/api/comments/**")
	                                         .uri("lb://comment-service")) // Load balancing verso il servizio registrato in Eureka
	        .build();
	}
	
	
	
	
	
	@Bean
	RouteLocator getUserRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("user-service", r -> r.path("/api/users/**", "/api/admins/**")
	                                      .uri("lb://user-service")) // Load balancing verso user-service
	        .build();
	}

	
	
	@Bean
	RouteLocator getPhotoRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("photo-service", r -> r.path("/api/photos/**")
	                                         .uri("lb://photo-service")) // Load balancing verso il servizio registrato in Eureka
	        .build();
	}


}
