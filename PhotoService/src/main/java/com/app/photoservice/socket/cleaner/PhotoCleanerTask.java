package com.app.photoservice.socket.cleaner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.photoservice.socket.PhotoWebSocketHandler;

@Component
public class PhotoCleanerTask {

    private static final Logger logger = LoggerFactory.getLogger(PhotoCleanerTask.class);

    private final PhotoWebSocketHandler photoWebSocketHandler;

    public PhotoCleanerTask(PhotoWebSocketHandler photoWebSocketHandler) {
        this.photoWebSocketHandler = photoWebSocketHandler;
    }

    @Scheduled(fixedRate = 60000) // Ogni 60 secondi
    public void cleanStaleSessions() {
        logger.info("Avvio della pulizia delle sessioni inattive...");
        photoWebSocketHandler.checkAndCleanStaleSessions();
    }
}
