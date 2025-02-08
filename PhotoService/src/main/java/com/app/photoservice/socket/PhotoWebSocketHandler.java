package com.app.photoservice.socket;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

 

@Component
public class PhotoWebSocketHandler implements WebSocketHandler {

	 private static final Logger logger = LoggerFactory.getLogger(PhotoWebSocketHandler.class);

	    private final Map<String, Sinks.Many<String>> sessions = Collections.synchronizedMap(new HashMap<>());

	    @Override
	    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
	        String sessionId = session.getId();
	        logger.info("Connessione stabilita per il WebSocket delle foto: {}", sessionId);

	        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
	        sessions.put(sessionId, sink);

	        Flux<WebSocketMessage> messageFlux = sink.asFlux()
	                .map(session::textMessage);

	        return session.send(messageFlux)
	                .doOnTerminate(() -> {
	                    sessions.remove(sessionId);
	                    logger.info("Connessione chiusa per il WebSocket delle foto: {}", sessionId);
	                })
	                .doOnError(error -> logger.error("Errore WebSocket per le foto: {}", error.getMessage()));
	    }

	    public void notifyNewPhoto(String photoId) {
	        String message = "New photo uploaded: " + photoId;
	        synchronized (sessions) {
	            sessions.values().forEach(sink -> sink.tryEmitNext(message));
	        }
	        logger.info("Notifica inviata per nuova foto con ID: {}", photoId);
	    }
}
