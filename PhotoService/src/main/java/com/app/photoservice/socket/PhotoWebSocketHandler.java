package com.app.photoservice.socket;

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

//@Component
//public class PhotoWebSocketHandler implements WebSocketHandler {
//
//    private static final Logger logger = LoggerFactory.getLogger(PhotoWebSocketHandler.class);
//
//    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
//    private final ConcurrentMap<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
//
//    @Override
//    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
//        activeSessions.put(session.getId(), session);
//        logger.info("Connessione stabilita: {}", session.getId());
//
//        Flux<WebSocketMessage> messageFlux = sink.asFlux()
//                .map(session::textMessage);
//
//        return session.send(messageFlux)
//                .doOnTerminate(() -> {
//                    activeSessions.remove(session.getId());
//                    logger.info("Connessione terminata: {}", session.getId());
//                    logger.info("Sessioni attive: {}", activeSessions.size());
//                })
//                .doOnError(error -> {
//                    activeSessions.remove(session.getId());
//                    logger.error("Errore nella connessione WebSocket: {}", error.getMessage());
//                });
//    }
//
//    public void notifyNewPhoto(String photoId) {
//        // Notifica tutti i client attivi
//        sink.tryEmitNext("New photo uploaded: " + photoId);
//        logger.info("Notifica inviata per nuova foto con ID: {}", photoId);
//    }
//
//
////    public void checkAndCleanStaleSessions() {
////        activeSessions.forEach((id, session) -> {
////            if (!session.isOpen()) {
////                activeSessions.remove(id);
////                logger.warn("Rimossa sessione non valida: {}", id);
////            }
////        });
////        logger.info("Pulizia completata. Sessioni attive: {}", activeSessions.size());
////    }
//   
//}   

@Component
public class PhotoWebSocketHandler implements WebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(PhotoWebSocketHandler.class);

	private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

	@Override
	public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
		logger.info("Connessione stabilita per il WebSocket delle foto: {}", session.getId());

		Flux<WebSocketMessage> messageFlux = sink.asFlux().map(session::textMessage);

		return session.send(messageFlux).doFinally(
				signalType -> logger.info("Connessione chiusa per il WebSocket delle foto: {}", session.getId()));
	}

	public void notifyNewPhoto(String photoId) {
		sink.tryEmitNext("New photo uploaded: " + photoId);
		logger.info("Notifica inviata per nuova foto con ID: {}", photoId);
	}
}
