package com.helpdesk.ticket.dto;

import jakarta.validation.constraints.NotBlank;

public record TicketCommentCreateDTO(
        @NotBlank(message = "O conteúdo do comentário não pode estar vazio")
        String content
) {}