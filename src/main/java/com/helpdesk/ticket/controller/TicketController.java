package com.helpdesk.ticket.controller;

import com.helpdesk.ticket.domain.TicketPriority;
import com.helpdesk.ticket.domain.TicketStatus;
import com.helpdesk.ticket.dto.TicketCommentCreateDTO;
import com.helpdesk.ticket.dto.TicketCommentResponseDTO;
import com.helpdesk.ticket.dto.TicketCreateDTO;
import com.helpdesk.ticket.dto.TicketResponseDTO;
import com.helpdesk.ticket.dto.TicketStatusUpdateDTO;
import com.helpdesk.ticket.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        String userEmail = authentication.getName();
        TicketResponseDTO response = ticketService.create(dto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TicketResponseDTO>> findAll(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<TicketResponseDTO> tickets = ticketService.findAll(status, priority, pageable);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<TicketCommentResponseDTO> addComment(
            @PathVariable Long id,
            @RequestBody @Valid TicketCommentCreateDTO dto,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        TicketCommentResponseDTO response = ticketService.addComment(id, dto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<TicketCommentResponseDTO>> getComments(@PathVariable Long id) {
        List<TicketCommentResponseDTO> comments = ticketService.getCommentsByTicket(id);
        return ResponseEntity.ok(comments);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid TicketStatusUpdateDTO dto
    ) {
        TicketResponseDTO response = ticketService.updateStatus(id, dto);
        return ResponseEntity.ok(response);
    }
}