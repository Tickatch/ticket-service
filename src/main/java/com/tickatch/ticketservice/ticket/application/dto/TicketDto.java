package com.tickatch.ticketservice.ticket.application.dto;

import com.tickatch.ticketservice.ticket.domain.Ticket;
import com.tickatch.ticketservice.ticket.domain.TicketStatus;
import java.util.UUID;

public record TicketDto(UUID id, String seatNumber, String grade, Long price, TicketStatus status) {
  public static TicketDto from(Ticket ticket) {
    return new TicketDto(
        ticket.getId().toUuid(),
        ticket.getSeatInfo().getSeatNumber(),
        ticket.getSeatInfo().getGrade(),
        ticket.getSeatInfo().getPrice(),
        ticket.getStatus());
  }
}
