package com.app.commentservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.commentservice.exception.CommentNotFoundException;
import com.app.commentservice.model.CommentResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommentResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        logger.error("Handled IllegalArgumentException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(
            CommentResponse.failure(ex.getMessage(), HttpStatus.BAD_REQUEST)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommentResponse<Void>> handleGeneralException(Exception ex) {
        logger.error("Handled Exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            CommentResponse.failure("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }
    
    
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<CommentResponse<Void>> handleCommentNotFoundException(CommentNotFoundException ex) {
        logger.error("Handled CommentNotFoundException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            CommentResponse.failure(ex.getMessage(), HttpStatus.NOT_FOUND)
        );
    }
}
