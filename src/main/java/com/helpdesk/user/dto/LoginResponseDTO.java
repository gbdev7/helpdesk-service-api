package com.helpdesk.user.dto;

public record LoginResponseDTO(
        String token,
        String type
) {
    public LoginResponseDTO(String token) {
        this(token, "Bearer");
    }
}