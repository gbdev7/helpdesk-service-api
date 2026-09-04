package com.helpdesk.ticket.service;

import com.helpdesk.ticket.domain.Ticket;
import com.helpdesk.ticket.dto.TicketCreateDTO;
import com.helpdesk.ticket.dto.TicketResponseDTO;
import com.helpdesk.ticket.repository.TicketRepository;
import com.helpdesk.user.domain.User;
import com.helpdesk.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TicketResponseDTO create(TicketCreateDTO dto, String userEmail) {
        User customer = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Ticket ticket = new Ticket(dto.title(), dto.description(), dto.priority(), customer);
        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketResponseDTO.fromEntity(savedTicket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDTO> findAll() {
        return ticketRepository.findAll()
                .stream()
                .map(TicketResponseDTO::fromEntity)
                .toList();
    }
}