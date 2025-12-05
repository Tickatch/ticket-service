package com.tickatch.ticketservice.ticket.presentation.dto;

import com.tickatch.ticketservice.ticket.domain.Ticket;
import com.tickatch.ticketservice.ticket.domain.TicketStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketDetailResponse(
    UUID id,
    UUID reservationId,
    long seatId,
    String seatNumber,
    String grade,
    Long price,
    TicketStatus status,
    LocalDateTime issuedAt) {
  public static TicketDetailResponse from(Ticket ticket) {
    return new TicketDetailResponse(
        ticket.getId().toUuid(),
        ticket.getReservationId(),
        ticket.getSeatInfo().getSeatId(),
        ticket.getSeatInfo().getSeatNumber(),
        ticket.getSeatInfo().getGrade(),
        ticket.getSeatInfo().getPrice(),
        ticket.getStatus(),
        ticket.getIssuedAt());
  }
}
