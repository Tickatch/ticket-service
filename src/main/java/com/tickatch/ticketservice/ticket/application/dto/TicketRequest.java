package com.tickatch.ticketservice.ticket.application.dto;

import com.tickatch.ticketservice.ticket.domain.ReceiveMethod;
import java.util.UUID;

public record TicketRequest(
    UUID reservationId,
    long seatId,
    long productId,
    String grade,
    String seatNumber,
    Long price,
    ReceiveMethod receiveMethod) {}
