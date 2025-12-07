package com.tickatch.ticketservice.ticket.presentation.dto;

import com.tickatch.ticketservice.ticket.application.dto.TicketRequest;
import com.tickatch.ticketservice.ticket.domain.ReceiveMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateTicketRequest(
    @NotNull UUID reservationId,
    Long seatId,
    Long productId,
    @NotBlank String seatNumber,
    @NotBlank String grade,
    Long price,
    ReceiveMethod receiveMethod) {
  public TicketRequest toTicketRequest() {
    return new TicketRequest(
        this.reservationId(),
        this.seatId(),
        this.productId(),
        this.grade(),
        this.seatNumber(),
        this.price(),
        this.receiveMethod
    );
  }
}
