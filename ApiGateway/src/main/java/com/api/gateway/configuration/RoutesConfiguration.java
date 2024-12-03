package com.api.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfiguration {
	
	
	

//	@Bean
//	RouteLocator getCommentRouteLocator(RouteLocatorBuilder builder) {
//	    return builder.routes()
//	        .route("comment-service", r -> r.path("/api/comments/**")
//	                                         .uri("lb://comment-service")) // Load balancing verso il servizio registrato in Eureka
//	        .build();
//	}
//	
//	
//	
//	
//	
//	@Bean
//	RouteLocator getUserRouteLocator(RouteLocatorBuilder builder) {
//	    return builder.routes()
//	        .route("user-service", r -> r.path("/api/users/**", "/api/admins/**")
//	                                      .uri("lb://user-service")) // Load balancing verso user-service
//	        .build();
//	}
//
//	
//	
//	@Bean
//	RouteLocator getPhotoRouteLocator(RouteLocatorBuilder builder) {
//	    return builder.routes()
//	        .route("photo-service", r -> r.path("/api/photos/**")
//	                                         .uri("lb://photo-service")) // Load balancing verso il servizio registrato in Eureka
//	        .build();
//	}
//	
//	
	@Value("${comment.service.uri}")
	private String commentServiceUri;

	@Value("${user.service.uri}")
	private String userServiceUri;

	@Value("${photo.service.uri}")
	private String photoServiceUri;

	@Bean
	RouteLocator userServiceRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes().route("user-service", r -> r.path("/api/users/**")
//	                .filters(f -> f
//	                    .circuitBreaker(c -> c.setName("userServiceCB").setFallbackUri("forward:/fallback/users")))
				.uri(userServiceUri)).build();
	}

	@Bean
	RouteLocator photoServiceRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes().route("photo-service", r -> r.path("/api/photos/**")
//	                .filters(f -> f
//	                    .circuitBreaker(c -> c.setName("photoServiceCB").setFallbackUri("forward:/fallback/photos")))
				.uri(photoServiceUri)).build();
	}

	@Bean
	RouteLocator commentServiceRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes().route("comment-service", r -> r.path("/api/comments/**")
//	                    .filters(f -> f
//	                        .circuitBreaker(c -> c.setName("commentServiceCB").setFallbackUri("forward:/fallback/comments")))
				.uri(commentServiceUri)).build();
	}
	
	
	@Bean
	RouteLocator apiDocsRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("swagger-user-service", r -> r
	            .path("/aggregate/user-service/v3/api-docs")
	            .filters(f -> f.setPath("/v3/api-docs"))
	            .uri(userServiceUri))
	        .route("swagger-photo-service", r -> r
	            .path("/aggregate/photo-service/v3/api-docs")
	            .filters(f -> f.setPath("/v3/api-docs"))
	            .uri(photoServiceUri))
	        .route("swagger-comment-service", r -> r
	            .path("/aggregate/comment-service/v3/api-docs")
	            .filters(f -> f.setPath("/v3/api-docs"))
	            .uri(commentServiceUri))
	        .build();
	}

	
	
	
	
	}

