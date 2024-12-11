package com.app.commentservice.socket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
public class CommentWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommentWebSocketHandler.class);

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final ConcurrentMap<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    @Override
    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
        // Aggiungi la sessione attiva
        activeSessions.put(session.getId(), session);
        logger.info("Connessione WebSocket stabilita: {}", session.getId());

        // Flux per inviare messaggi ai client
        Flux<WebSocketMessage> messageFlux = sink.asFlux()
                .map(session::textMessage);

        // Rimuovi la sessione quando si chiude
        return session.send(messageFlux)
                .doOnTerminate(() -> {
                    activeSessions.remove(session.getId());
                    logger.info("Connessione WebSocket terminata: {}", session.getId());
                    logger.info("Sessioni attive: {}", activeSessions.size());
                })
                .doOnError(error -> {
                    activeSessions.remove(session.getId());
                    logger.error("Errore nella connessione WebSocket: {}", error.getMessage());
                });
    }

    public void notifyCommentUpdate(String message) {
        // Notifica i client connessi
        sink.tryEmitNext(message);
        logger.info("Notifica inviata: {}", message);
    }

    public void checkAndCleanStaleSessions() {
        // Rimuovi le sessioni non valide
        activeSessions.forEach((id, session) -> {
            if (!session.isOpen()) {
                activeSessions.remove(id);
                logger.warn("Rimossa sessione non valida: {}", id);
            }
        });
        logger.info("Pulizia sessioni completata. Sessioni attive: {}", activeSessions.size());
    }
}
