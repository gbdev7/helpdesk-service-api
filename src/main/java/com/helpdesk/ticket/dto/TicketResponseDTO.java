package com.helpdesk.ticket.dto;

import com.helpdesk.ticket.domain.Ticket;
import com.helpdesk.ticket.domain.TicketPriority;
import com.helpdesk.ticket.domain.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponseDTO(
        Long id,
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority,
        LocalDateTime createdAt,
        LocalDateTime slaDueDate,
        String customerName,
        String technicianName
) {
    public static TicketResponseDTO fromEntity(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCreatedAt(),
                ticket.getSlaDueDate(),
                ticket.getCustomer() != null ? ticket.getCustomer().getName() : null,
                ticket.getTechnician() != null ? ticket.getTechnician().getName() : null
        );
    }
}