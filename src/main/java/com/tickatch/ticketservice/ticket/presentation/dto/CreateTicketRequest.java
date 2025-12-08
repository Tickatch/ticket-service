package com.tickatch.ticketservice.ticket.presentation.dto;

import com.tickatch.ticketservice.ticket.application.dto.TicketRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateTicketRequest(
    @NotNull UUID reservationId,
    Long seatId,
    @NotBlank String seatNumber,
    @NotBlank String grade,
    Long price,
    String receiveMethod) {
  public TicketRequest toTicketRequest() {
    return new TicketRequest(
        this.reservationId(),
        this.seatId(),
        this.grade(),
        this.seatNumber(),
        this.price(),
        this.receiveMethod);
  }
}
