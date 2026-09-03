package com.helpdesk.user.dto;

import com.helpdesk.user.domain.Role;
import com.helpdesk.user.domain.User;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Role role,
        LocalDateTime createdAt
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}