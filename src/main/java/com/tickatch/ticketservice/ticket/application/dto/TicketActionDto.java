package com.tickatch.ticketservice.ticket.application.dto;

import com.tickatch.ticketservice.ticket.domain.Ticket;
import java.util.UUID;

public record TicketActionDto(UUID id, String status, String usedAt, String deletedAt) {
  public static TicketActionDto fromUsed(Ticket ticket) {
    return new TicketActionDto(
        ticket.getId().toUuid(),
        ticket.getStatus().name(),
        ticket.getUsedAt() != null ? ticket.getUsedAt().toString() : null,
        null);
  }

  public static TicketActionDto fromCanceled(Ticket ticket) {
    return new TicketActionDto(
        ticket.getId().toUuid(),
        ticket.getStatus().name(),
        null,
        ticket.getDeletedAt() != null ? ticket.getDeletedAt().toString() : null);
  }
}
