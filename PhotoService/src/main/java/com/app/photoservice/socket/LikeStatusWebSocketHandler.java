package com.app.photoservice.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LikeStatusWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(LikeStatusWebSocketHandler.class);

    private final Sinks.Many<Map<Long, Boolean>> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    @Override
    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
        activeSessions.put(session.getId(), session);
        logger.info("Connessione stabilita per il WebSocket dei like: {}", session.getId());

        Flux<WebSocketMessage> messageFlux = sink.asFlux()
                .map(status -> session.textMessage(status.toString()));

        return session.send(messageFlux)
                .doOnTerminate(() -> {
                    activeSessions.remove(session.getId());
                    logger.info("Connessione chiusa per il WebSocket dei like: {}", session.getId());
                });
    }

    public void notifyLikeStatus(Long photoId, Boolean liked) {
        Map<Long, Boolean> likeStatus = Map.of(photoId, liked);
        sink.tryEmitNext(likeStatus);
        logger.info("Notifica inviata per il like della foto {}: {}", photoId, liked);
    }

    public void checkAndCleanStaleSessions() {
        activeSessions.forEach((id, session) -> {
            if (!session.isOpen()) {
                activeSessions.remove(id);
                logger.warn("Rimossa sessione non valida: {}", id);
            }
        });
        logger.info("Pulizia completata. Sessioni attive: {}", activeSessions.size());
    }
}
