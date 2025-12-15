package com.tickatch.ticketservice.ticket.application.dto;

import com.tickatch.ticketservice.ticket.domain.Ticket;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketActionResponse(
    UUID id, String status, LocalDateTime usedAt, LocalDateTime canceledAt) {
  public static TicketActionResponse fromUsed(Ticket ticket) {
    return new TicketActionResponse(
        ticket.getId().toUuid(), ticket.getStatus().name(), ticket.getUsedAt(), null);
  }

  public static TicketActionResponse fromCanceled(Ticket ticket) {
    return new TicketActionResponse(
        ticket.getId().toUuid(), ticket.getStatus().name(), null, ticket.getCanceledAt());
  }
}
