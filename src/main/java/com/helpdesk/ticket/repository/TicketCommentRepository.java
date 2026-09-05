package com.helpdesk.ticket.repository;

import com.helpdesk.ticket.domain.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
    List<TicketComment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}