package com.tickatch.ticketservice.ticket.domain.service;

import java.util.UUID;

public interface ReservationService {

  // 예매 확정 여부
  boolean isConfirmed(UUID reservationId);
}
