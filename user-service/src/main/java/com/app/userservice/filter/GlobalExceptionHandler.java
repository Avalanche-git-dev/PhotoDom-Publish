package com.app.userservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.app.userservice.exception.*;
import com.app.userservice.model.UserResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Metodo generico per creare risposte di errore uniformi
    private ResponseEntity<UserResponse<Void>> buildErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(UserResponse.failure(message, status));
    }

    // Eccezione generica per User
    @ExceptionHandler(UserException.class)
    public ResponseEntity<UserResponse<Void>> handleUserException(UserException ex) {
        logger.error("UserException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Utente non trovato
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserResponse<Void>> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("UserNotFoundException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Email duplicata
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<UserResponse<Void>> handleDuplicateEmailException(DuplicateEmailException ex) {
        logger.error("DuplicateEmailException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Username duplicato
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<UserResponse<Void>> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        logger.error("DuplicateUsernameException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }
    
    // Duplicazione generale
    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<UserResponse<Void>> handleDuplicateFieldException(DuplicateFieldException ex) {
        logger.error("DuplicateFieldException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Campo non valido
    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<UserResponse<Void>> handleInvalidFieldException(InvalidFieldException ex) {
        logger.error("InvalidFieldException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Admin già esistente
    @ExceptionHandler(AdminAlreadyExistsException.class)
    public ResponseEntity<UserResponse<Void>> handleAdminAlreadyExistsException(AdminAlreadyExistsException ex) {
        logger.error("AdminAlreadyExistsException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Stato non valido
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<UserResponse<Void>> handleIllegalStateException(IllegalStateException ex) {
        logger.error("IllegalStateException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Utente non autorizzato
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<UserResponse<Void>> handleNotAuthorizedException(NotAuthorizedException ex) {
        logger.error("NotAuthorizedException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Violazione della struttura del database
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<UserResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("DataIntegrityViolationException: {}", ex.getMessage(), ex);
        return buildErrorResponse("Data integrity error: " + ex.getMostSpecificCause().getMessage(), HttpStatus.CONFLICT);
    }

    // Gestione errori generici
    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserResponse<Void>> handleGeneralException(Exception ex) {
        logger.error("General Exception: {}", ex.getMessage(), ex);
        return buildErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

