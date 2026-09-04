package com.helpdesk.ticket.domain;

public enum TicketPriority {
    LOW(48),       // SLA de 48 horas
    MEDIUM(24),    // SLA de 24 horas
    HIGH(8),       // SLA de 8 horas
    CRITICAL(2);   // SLA de 2 horas

    private final int slaHours;

    TicketPriority(int slaHours) {
        this.slaHours = slaHours;
    }

    public int getSlaHours() {
        return slaHours;
    }
}