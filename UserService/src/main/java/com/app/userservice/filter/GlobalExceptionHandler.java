package com.app.userservice.filter;

//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    @ExceptionHandler(UserException.class)
//    public ResponseEntity<String> handleUserException(UserException ex) {
//        logger.error("Handled UserException: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
//        logger.error("Handled UserNotFoundException: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(DuplicateEmailException.class)
//    public ResponseEntity<String> handleDuplicateEmailException(DuplicateEmailException ex) {
//        logger.error("Handled DuplicateEmailException: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(DuplicateUsernameException.class)
//    public ResponseEntity<String> handleDuplicateUsernameException(DuplicateUsernameException ex) {
//        logger.error("Handled DuplicateUsernameException: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(InvalidFieldException.class)
//    public ResponseEntity<String> handleInvalidFieldException(InvalidFieldException ex) {
//        logger.error("Handled InvalidFieldException: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(AdminAlreadyExistsException.class)
//    public ResponseEntity<String> handleAdminAlreadyExistsException(AdminAlreadyExistsException ex) {
//        logger.error("Handled AdminAlreadyExistsException: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
//    }
//    
//    
//    @ExceptionHandler(IllegalStateException.class)
//    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
//        logger.error("Handled IllegalStateException: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
//    }
//    
//    
//    @ExceptionHandler(NotAuthorizedException.class)
//    public ResponseEntity<String> handleNotAuthorizedException(NotAuthorizedException ex) {
//        logger.error("Handled NotAuthorizedException: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
//    }
//    
//    
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
//        logger.error("Handled DataIntegrityViolationException: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>("Data integrity error: " + ex.getMostSpecificCause().getMessage(), HttpStatus.CONFLICT);
//    }
//
//    
//    
//}





	

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.app.userservice.exception.AdminAlreadyExistsException;
import com.app.userservice.exception.DuplicateEmailException;
import com.app.userservice.exception.DuplicateUsernameException;
import com.app.userservice.exception.InvalidFieldException;
import com.app.userservice.exception.NotAuthorizedException;
import com.app.userservice.exception.UserException;
import com.app.userservice.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        errorResponse.put("status", status.value());
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(UserException ex) {
        logger.error("Handled UserException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("Handled UserNotFoundException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmailException(DuplicateEmailException ex) {
        logger.error("Handled DuplicateEmailException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        logger.error("Handled DuplicateUsernameException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFieldException(InvalidFieldException ex) {
        logger.error("Handled InvalidFieldException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AdminAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAdminAlreadyExistsException(AdminAlreadyExistsException ex) {
        logger.error("Handled AdminAlreadyExistsException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException ex) {
        logger.error("Handled IllegalStateException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleNotAuthorizedException(NotAuthorizedException ex) {
        logger.error("Handled NotAuthorizedException: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("Handled DataIntegrityViolationException: {}", ex.getMessage(), ex);
        return buildErrorResponse("Data integrity error: " + ex.getMostSpecificCause().getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        logger.error("Handled General Exception: {}", ex.getMessage(), ex);
        return buildErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



