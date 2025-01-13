package com.app.photoservice.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.photoservice.dto.PhotoResponse;
import com.app.photoservice.exception.PhotoNotFoundException;
import com.app.photoservice.exception.PhotoReadingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(PhotoNotFoundException.class)
	public ResponseEntity<PhotoResponse<Void>> handlePhotoNotFoundException(PhotoNotFoundException ex) {
		logger.error("Handled PhotoNotFoundException: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(PhotoResponse.failure("Photo not found", HttpStatus.NOT_FOUND));
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<PhotoResponse<Void>> handleIOException(IOException ex) {
		logger.error("Handled IOException: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(PhotoResponse
				.failure("Errore durante l'elaborazione della richiesta.", HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<PhotoResponse<Void>> handleIllegalStateException(IllegalStateException ex) {
		logger.error("Handled IllegalStateException: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(PhotoResponse.failure("Stato illegale rilevato.", HttpStatus.CONFLICT));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<PhotoResponse<Void>> handleRuntimeException(RuntimeException ex) {
		logger.error("Handled RuntimeException: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(PhotoResponse.failure("Errore di runtime.", HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<PhotoResponse<Void>> handleGeneralException(Exception ex) {
		logger.error("Handled Exception: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(PhotoResponse.failure(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(PhotoReadingException.class)
	public ResponseEntity<PhotoResponse<Void>> handlePhotoReadingException(PhotoReadingException ex) {
		logger.error("Handled PhotoReadingException: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(PhotoResponse
				.failure("Errore durante lettura della foto durante il caricamento", HttpStatus.INTERNAL_SERVER_ERROR));
	}
	
	
	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<PhotoResponse<Void>> handleSecurityException(SecurityException ex) {
		logger.error("Handled SecurityException: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(PhotoResponse
				.failure("Non sei autorizzato ad eliminare il contenuto ", HttpStatus.FORBIDDEN));
	}
}
