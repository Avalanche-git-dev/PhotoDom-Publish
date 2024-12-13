package com.app.userservice.model;


import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
    boolean success,
    String message,
    int status,
    T data
) {

    public static <T> ApiResponse<T> success(String message, HttpStatus status, T data) {
        return new ApiResponse<>(true, message, status.value(), data);
    }

    public static ApiResponse<Void> success(String message, HttpStatus status) {
        return new ApiResponse<>(true, message, status.value(), null);
    }

    public static <T> ApiResponse<T> failure(String error, HttpStatus status, T data) {
        return new ApiResponse<>(false, error, status.value(), data);
    }

    public static ApiResponse<Void> failure(String error, HttpStatus status) {
        return new ApiResponse<>(false, error, status.value(), null);
    }
}
