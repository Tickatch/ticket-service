package com.tickatch.ticketservice.ticket.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tickatch.ticketservice.ticket.domain.dto.ReservationInfo;
import java.util.UUID;

public record ReservationClientResponse(
    @JsonProperty("id") UUID reservationId,
    String reserverName,
    UUID reserverId,
    String reservationNumber) {

  public ReservationInfo toReservationInfo() {
    return new ReservationInfo(reservationId, reserverName, reserverId, reservationNumber);
  }
}
