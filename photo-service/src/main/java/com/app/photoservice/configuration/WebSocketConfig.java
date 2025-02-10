package com.app.photoservice.configuration;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.app.photoservice.socket.LikeStatusWebSocketHandler;
import com.app.photoservice.socket.PhotoWebSocketHandler;

@Configuration
public class WebSocketConfig {

	private final PhotoWebSocketHandler photoWebSocketHandler;

	private final LikeStatusWebSocketHandler likeStatusWebSocketHandler;

	public WebSocketConfig(PhotoWebSocketHandler photoWebSocketHandler,
			LikeStatusWebSocketHandler likeStatusWebSocketHandler) {
		this.photoWebSocketHandler = photoWebSocketHandler;
		this.likeStatusWebSocketHandler = likeStatusWebSocketHandler;
	}

	@Bean
	public HandlerMapping webSocketHandlerMapping() {
		return new SimpleUrlHandlerMapping(
				Map.of("/ws/photos", photoWebSocketHandler, "/ws/like/status", likeStatusWebSocketHandler), 1); // Imposta
																												// la
																												// priorità
																												// globale
																												// del
																												// mapping
	}

	@Bean
	public WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
}
