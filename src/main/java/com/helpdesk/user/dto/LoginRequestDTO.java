package com.helpdesk.user.dto;

public record LoginRequestDTO(
        String email,
        String password
) {}