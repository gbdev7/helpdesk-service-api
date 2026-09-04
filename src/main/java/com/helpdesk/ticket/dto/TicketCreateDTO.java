package com.helpdesk.ticket.dto;

import com.helpdesk.ticket.domain.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketCreateDTO(
        @NotBlank(message = "O título é obrigatório")
        String title,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @NotNull(message = "A prioridade é obrigatória")
        TicketPriority priority
) {}