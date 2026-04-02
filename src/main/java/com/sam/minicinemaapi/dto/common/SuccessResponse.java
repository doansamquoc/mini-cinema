package com.sam.minicinemaapi.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SuccessResponse<T>(boolean success, String message, T data, LocalDateTime timestamp) {
    public static <T> SuccessResponse<T> ofData(T data) {
        return new SuccessResponse<>(true, "Success", data, LocalDateTime.now());
    }

    public static <T> SuccessResponse<T> of(String message, T data) {
        return new SuccessResponse<>(true, message, data, LocalDateTime.now());
    }

    public static <T> SuccessResponse<T> ofMessage(String message) {
        return new SuccessResponse<>(true, message, null, LocalDateTime.now());
    }

    public static <T> SuccessResponse<T> of() {
        return new SuccessResponse<>(true, "Success", null, LocalDateTime.now());
    }
}