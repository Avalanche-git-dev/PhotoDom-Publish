package com.app.photoservice.socket.cleaner;

//@Component
//public class PhotoCleanerTask {
//
//    private static final Logger logger = LoggerFactory.getLogger(PhotoCleanerTask.class);
//
//    private final PhotoWebSocketHandler photoWebSocketHandler;
//
//    public PhotoCleanerTask(PhotoWebSocketHandler photoWebSocketHandler) {
//        this.photoWebSocketHandler = photoWebSocketHandler;
//    }
//
//    @Scheduled(fixedRate = 86400000) // Ogni 60 secondi
//    public void cleanStaleSessions() {
//        logger.info("Avvio della pulizia delle sessioni inattive...");
//        photoWebSocketHandler.checkAndCleanStaleSessions();
//    }
//}
