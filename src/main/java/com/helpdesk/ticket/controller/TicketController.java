package com.helpdesk.ticket.controller;

import com.helpdesk.ticket.dto.TicketCreateDTO;
import com.helpdesk.ticket.dto.TicketResponseDTO;
import com.helpdesk.ticket.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> create(
            @RequestBody @Valid TicketCreateDTO dto,
            Authentication authentication
    ) {
        // authentication.getName() retorna o e-mail extraído do Token JWT no SecurityFilter
        String userEmail = authentication.getName();
        TicketResponseDTO response = ticketService.create(dto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> findAll() {
        List<TicketResponseDTO> tickets = ticketService.findAll();
        return ResponseEntity.ok(tickets);
    }
}