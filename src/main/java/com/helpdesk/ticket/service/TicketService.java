package com.helpdesk.ticket.service;

import com.helpdesk.shared.exception.ResourceNotFoundException;
import com.helpdesk.ticket.domain.Ticket;
import com.helpdesk.ticket.domain.TicketComment;
import com.helpdesk.ticket.domain.TicketPriority;
import com.helpdesk.ticket.domain.TicketStatus;
import com.helpdesk.ticket.dto.TicketCommentCreateDTO;
import com.helpdesk.ticket.dto.TicketCommentResponseDTO;
import com.helpdesk.ticket.dto.TicketCreateDTO;
import com.helpdesk.ticket.dto.TicketResponseDTO;
import com.helpdesk.ticket.dto.TicketStatusUpdateDTO;
import com.helpdesk.ticket.repository.TicketCommentRepository;
import com.helpdesk.ticket.repository.TicketRepository;
import com.helpdesk.user.domain.User;
import com.helpdesk.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketCommentRepository commentRepository;

    public TicketService(
            TicketRepository ticketRepository,
            UserRepository userRepository,
            TicketCommentRepository commentRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public TicketResponseDTO create(TicketCreateDTO dto, String userEmail) {
        User customer = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com e-mail: " + userEmail));

        Ticket ticket = new Ticket(dto.title(), dto.description(), dto.priority(), customer);
        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketResponseDTO.fromEntity(savedTicket);
    }

    @Transactional(readOnly = true)
    public Page<TicketResponseDTO> findAll(TicketStatus status, TicketPriority priority, Pageable pageable) {
        return ticketRepository.findByFilters(status, priority, pageable)
                .map(TicketResponseDTO::fromEntity);
    }

    @Transactional
    public TicketCommentResponseDTO addComment(Long ticketId, TicketCommentCreateDTO dto, String userEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado com ID: " + ticketId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com e-mail: " + userEmail));

        TicketComment comment = new TicketComment(dto.content(), ticket, user);
        TicketComment savedComment = commentRepository.save(comment);

        return TicketCommentResponseDTO.fromEntity(savedComment);
    }

    @Transactional(readOnly = true)
    public List<TicketCommentResponseDTO> getCommentsByTicket(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new ResourceNotFoundException("Chamado não encontrado com ID: " + ticketId);
        }

        return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId)
                .stream()
                .map(TicketCommentResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public TicketResponseDTO updateStatus(Long ticketId, TicketStatusUpdateDTO dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado com ID: " + ticketId));

        ticket.setStatus(dto.status());

        if (dto.technicianId() != null) {
            User technician = userRepository.findById(dto.technicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado com ID: " + dto.technicianId()));
            ticket.setTechnician(technician);
        }

        ticket.setUpdatedAt(LocalDateTime.now());
        Ticket updatedTicket = ticketRepository.save(ticket);

        return TicketResponseDTO.fromEntity(updatedTicket);
    }
}