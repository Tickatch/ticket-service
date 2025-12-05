package com.tickatch.ticketservice.ticket.presentation.dto;

import java.util.UUID;

public record CreateTicketRequest(
    UUID reservationId,
    UUID seatId,
    String seatNumber,
    String grade,
    Long price,
    String receiveMethod
) {

}
