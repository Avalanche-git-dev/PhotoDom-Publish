package com.app.commentservice.socket;

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

    @Override
    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
        logger.info("Connessione WebSocket per i commenti stabilita: {}", session.getId());

        // Flux per inviare messaggi al client
        Flux<WebSocketMessage> messageFlux = sink.asFlux()
                .map(session::textMessage);

        // Gestione invio messaggi e chiusura sessione
        return session.send(messageFlux)
                .doOnTerminate(() -> logger.info("Connessione WebSocket per i commenti chiusa: {}", session.getId()))
                .doOnError(error -> logger.error("Errore nella connessione WebSocket: {}", error.getMessage()));
    }

    public void notifyCommentUpdate(String message) {
        sink.tryEmitNext(message);
        logger.info("Notifica commento inviata: {}", message);
    }
}
