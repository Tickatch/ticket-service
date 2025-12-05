package com.tickatch.ticketservice.ticket.presentation.dto;

import com.tickatch.ticketservice.ticket.domain.Ticket;
import com.tickatch.ticketservice.ticket.domain.TicketStatus;
import java.util.UUID;

public record TicketResponse(
    UUID id, String seatNumber, String grade, Long price, TicketStatus status) {
  public static TicketResponse from(Ticket ticket) {
    return new TicketResponse(
        ticket.getId().toUuid(),
        ticket.getSeatInfo().getSeatNumber(),
        ticket.getSeatInfo().getGrade(),
        ticket.getSeatInfo().getPrice(),
        ticket.getStatus());
  }
}
