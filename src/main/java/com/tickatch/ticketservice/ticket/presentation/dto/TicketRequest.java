package com.tickatch.ticketservice.ticket.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record TicketRequest(
    @NotNull UUID reservationId,
    long seatId,
    @NotBlank String grade,
    @NotBlank String seatNumber,
    Long price) {}
