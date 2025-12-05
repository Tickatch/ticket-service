package com.tickatch.ticketservice.ticket.application.dto;

import java.util.UUID;

public record TicketCreateCommand(
    UUID reservationId, long seatId, String grade, String seatNumber, Long price) {}
