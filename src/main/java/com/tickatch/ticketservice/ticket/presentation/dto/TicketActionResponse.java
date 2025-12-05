package com.tickatch.ticketservice.ticket.presentation.dto;

import com.tickatch.ticketservice.ticket.domain.Ticket;
import java.util.UUID;

public record TicketActionResponse(UUID id, String status, String usedAt, String deletedAt) {
  public static TicketActionResponse fromUsed(Ticket ticket) {
    return new TicketActionResponse(
        ticket.getId().toUuid(),
        ticket.getStatus().name(),
        ticket.getUsedAt() != null ? ticket.getUsedAt().toString() : null,
        null);
  }

  public static TicketActionResponse fromCanceled(Ticket ticket) {
    return new TicketActionResponse(
        ticket.getId().toUuid(),
        ticket.getStatus().name(),
        null,
        ticket.getDeletedAt() != null ? ticket.getDeletedAt().toString() : null);
  }
}
