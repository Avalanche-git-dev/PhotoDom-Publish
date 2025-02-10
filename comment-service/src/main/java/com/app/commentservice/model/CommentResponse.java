package com.app.commentservice.model;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentResponse<T>(
    boolean success,
    String message,
    int status,
    T data
) {
    public static <T> CommentResponse<T> success(String message, HttpStatus status, T data) {
        return new CommentResponse<>(true, message, status.value(), data);
    }

    public static CommentResponse<Void> success(String message, HttpStatus status) {
        return new CommentResponse<>(true, message, status.value(), null);
    }

    public static <T> CommentResponse<T> failure(String message, HttpStatus status, T data) {
        return new CommentResponse<>(false, message, status.value(), data);
    }

    public static CommentResponse<Void> failure(String message, HttpStatus status) {
        return new CommentResponse<>(false, message, status.value(), null);
    }
}