package com.app.photoservice.socket.cleaner;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.photoservice.socket.LikeStatusWebSocketHandler;

@Component
public class LikeStatusCleanerTask {

    private final LikeStatusWebSocketHandler likeStatusWebSocketHandler;

    public LikeStatusCleanerTask(LikeStatusWebSocketHandler likeStatusWebSocketHandler) {
        this.likeStatusWebSocketHandler = likeStatusWebSocketHandler;
    }

    @Scheduled(fixedRate = 60000) // Ogni 60 secondi
    public void cleanStaleSessions() {
        likeStatusWebSocketHandler.checkAndCleanStaleSessions();
    }
}
