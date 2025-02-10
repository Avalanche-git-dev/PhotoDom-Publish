package com.app.commentservice.socket;

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
public class CommentWebSocketHandler implements WebSocketHandler {

  
	
	
    private static final Logger logger = LoggerFactory.getLogger(CommentWebSocketHandler.class);

    private final Map<String, Sinks.Many<String>> sessions = Collections.synchronizedMap(new HashMap<>());

    @Override
    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
        String sessionId = session.getId();
        logger.info("Connessione WebSocket per i commenti stabilita: {}", sessionId);

        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
        sessions.put(sessionId, sink);

        Flux<WebSocketMessage> messageFlux = sink.asFlux()
                .map(session::textMessage);

        return session.send(messageFlux)
                .doOnTerminate(() -> {
                    sessions.remove(sessionId);
                    logger.info("Connessione WebSocket per i commenti chiusa: {}", sessionId);
                })
                .doOnError(error -> logger.error("Errore WebSocket per i commenti: {}", error.getMessage()));
    }

    public void notifyCommentUpdate(String message) {
        synchronized (sessions) {
            sessions.values().forEach(sink -> sink.tryEmitNext(message));
        }
        logger.info("Notifica commento inviata a tutte le sessioni: {}", message);
    }
}
  

