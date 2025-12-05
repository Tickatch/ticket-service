package com.tickatch.ticketservice.ticket.presentation.dto;

import com.tickatch.ticketservice.ticket.application.dto.TicketCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record TicketRequest(
    @NotNull UUID reservationId,
    long seatId,
    @NotBlank String grade,
    @NotBlank String seatNumber,
    Long price)
{
  public TicketCreateCommand toCommand() {
    return new TicketCreateCommand(
        reservationId,
        seatId,
        grade,
        seatNumber,
        price
    );
  }
}
