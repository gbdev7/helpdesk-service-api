package com.helpdesk.ticket.dto;

import com.helpdesk.ticket.domain.TicketComment;
import java.time.LocalDateTime;

public record TicketCommentResponseDTO(
        Long id,
        String content,
        String userName,
        LocalDateTime createdAt
) {
    public static TicketCommentResponseDTO fromEntity(TicketComment comment) {
        return new TicketCommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName(),
                comment.getCreatedAt()
        );
    }
}