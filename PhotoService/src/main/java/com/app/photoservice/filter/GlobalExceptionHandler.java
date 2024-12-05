package com.app.photoservice.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.photoservice.exception.PhotoNotFoundException;
import com.app.photoservice.exception.PhotoReadingException;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger  = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	

    // Gestione personalizzata per PhotoNotFoundException
    @ExceptionHandler(PhotoNotFoundException.class)
    public ResponseEntity<String> handlePhotoNotFoundException(PhotoNotFoundException ex) {
    	logger.error("Handled PhotoNotFoundException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Gestione personalizzata per IOException
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
    	logger.error("Handled IOException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Errore durante l'elaborazione della richiesta: " + ex.getMessage());
    }

    // Gestione per IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
    	logger.error("Handled IllegalStateException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body("Stato illegale rilevato: " + ex.getMessage());
    }

    // Gestione generica per tutte le altre eccezioni
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
    	logger.error("Handled Exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Si è verificato un errore inatteso: " + ex.getMessage());
    }
    
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGeneralException(RuntimeException ex) {
    	logger.error("Handled RuntimeException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Si è verificato un errore inatteso: " + ex.getMessage());
    }
    
    
    
    @ExceptionHandler(PhotoReadingException.class)
    public ResponseEntity<String> handlePhotoReadingException(PhotoReadingException ex) {
    	logger.error("Handled PhotoReadingException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Si è verificato un errore inatteso: " + ex.getMessage());
    }
    
}
