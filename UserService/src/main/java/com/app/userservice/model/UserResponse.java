package com.app.userservice.model;


import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse<T>(
    boolean success,
    String message,
    int status,
    T data
) {

    public static <T> UserResponse<T> success(String message, HttpStatus status, T data) {
        return new UserResponse<>(true, message, status.value(), data);
    }

    public static UserResponse<Void> success(String message, HttpStatus status) {
        return new UserResponse<>(true, message, status.value(), null);
    }

    public static <T> UserResponse<T> failure(String error, HttpStatus status, T data) {
        return new UserResponse<>(false, error, status.value(), data);
    }

    public static UserResponse<Void> failure(String error, HttpStatus status) {
        return new UserResponse<>(false, error, status.value(), null);
    }
}
