package com.helpdesk.shared.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record RestErrorMessage(
        HttpStatus status,
        Integer statusCode,
        String message,
        LocalDateTime timestamp
) {
    public RestErrorMessage(HttpStatus status, String message) {
        this(status, status.value(), message, LocalDateTime.now());
    }
}