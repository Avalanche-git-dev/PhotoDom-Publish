package com.app.photoservice.dto;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PhotoResponse<T>(boolean success, String message, int status, T data) {

	public static <T> PhotoResponse<T> success(String message, HttpStatus status, T data) {
		return new PhotoResponse<>(true, message, status.value(), data);
	}

	public static PhotoResponse<Void> success(String message, HttpStatus status) {
		return new PhotoResponse<>(true, message, status.value(), null);
	}

	public static <T> PhotoResponse<T> failure(String error, HttpStatus status, T data) {
		return new PhotoResponse<>(false, error, status.value(), data);
	}

	public static PhotoResponse<Void> failure(String error, HttpStatus status) {
		return new PhotoResponse<>(false, error, status.value(), null);
	}
}
