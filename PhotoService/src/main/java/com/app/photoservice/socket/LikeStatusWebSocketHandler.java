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
public class LikeStatusWebSocketHandler implements WebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(LikeStatusWebSocketHandler.class);

    private final Map<String, Sinks.Many<Map<Long, Boolean>>> sessions = Collections.synchronizedMap(new HashMap<>());

    @Override
    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
        String sessionId = session.getId();
        logger.info("Connessione stabilita per il WebSocket dei like: {}", sessionId);

        Sinks.Many<Map<Long, Boolean>> sink = Sinks.many().multicast().onBackpressureBuffer();
        sessions.put(sessionId, sink);

        Flux<WebSocketMessage> messageFlux = sink.asFlux()
                .map(status -> session.textMessage(status.toString()));

        return session.send(messageFlux)
                .doOnTerminate(() -> {
                    sessions.remove(sessionId);
                    logger.info("Connessione chiusa per il WebSocket dei like: {}", sessionId);
                })
                .doOnError(error -> logger.error("Errore WebSocket LikeStatus: {}", error.getMessage()));
    }

    public void notifyLikeStatus(Long photoId, Boolean liked) {
        Map<Long, Boolean> likeStatus = Map.of(photoId, liked);
        synchronized (sessions) {
            sessions.values().forEach(sink -> sink.tryEmitNext(likeStatus));
        }
        logger.info("Notifica inviata per il like della foto {}: {}", photoId, liked);
    }
}

