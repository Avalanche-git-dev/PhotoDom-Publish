package com.app.photoservice.configuration;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.photoservice.exception.PhotoNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Gestione personalizzata per PhotoNotFoundException
    @ExceptionHandler(PhotoNotFoundException.class)
    public ResponseEntity<String> handlePhotoNotFoundException(PhotoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Gestione personalizzata per IOException
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Errore durante l'elaborazione della richiesta: " + ex.getMessage());
    }

    // Gestione per IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body("Stato illegale rilevato: " + ex.getMessage());
    }

    // Gestione generica per tutte le altre eccezioni
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Si è verificato un errore inatteso: " + ex.getMessage());
    }
}
