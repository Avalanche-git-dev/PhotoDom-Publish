package com.app.userservice.socket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class UserWebSocketHandler extends TextWebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(UserWebSocketHandler.class);
	private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

	@Override
	public void afterConnectionEstablished(@NonNull WebSocketSession session) {
		sessions.add(session);
		logger.info("WebSocket connection established: {}", session.getId());
	}

	@Override
	public void afterConnectionClosed(@NonNull WebSocketSession session,
			@SuppressWarnings("null") org.springframework.web.socket.CloseStatus status) {
		sessions.remove(session);
		logger.info("WebSocket connection closed: {}", session.getId());
	}

	public void notifyUserUpdate(String message) {
		synchronized (sessions) {
			sessions.forEach(session -> {
				try {
					session.sendMessage(new TextMessage(message));
				} catch (IOException e) {
					logger.error("Error sending message to WebSocket session {}: {}", session.getId(), e.getMessage());
				}
			});
		}
	}

	public void notifyUserChange(String message) {
		synchronized (sessions) {
			sessions.forEach(session -> {
				try {
					session.sendMessage(new TextMessage(message));
				} catch (IOException e) {
					logger.error("Error sending message to WebSocket session {}: {}", session.getId(), e.getMessage());
				}
			});
		}
	}

}

