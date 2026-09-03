package com.helpdesk.user.dto;

import com.helpdesk.user.domain.Role;

public record UserCreateDTO(
        String name,
        String email,
        String password,
        Role role
) {}