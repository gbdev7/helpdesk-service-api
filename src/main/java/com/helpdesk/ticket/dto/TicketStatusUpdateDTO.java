package com.helpdesk.ticket.dto;

import com.helpdesk.ticket.domain.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record TicketStatusUpdateDTO(
        @NotNull(message = "O novo status é obrigatório")
        TicketStatus status,
        Long technicianId // Opcional: ID do técnico atribuído
) {}