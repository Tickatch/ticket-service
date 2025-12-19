package com.tickatch.ticketservice.ticket.domain.dto;

import java.util.UUID;

public record ReservationInfo(
    UUID reservationId, String reservationNumber, UUID reserverId, String reserverName) {}
