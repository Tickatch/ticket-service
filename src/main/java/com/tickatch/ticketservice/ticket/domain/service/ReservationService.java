package com.tickatch.ticketservice.ticket.domain.service;

import com.tickatch.ticketservice.ticket.domain.dto.ReservationInfo;
import java.util.UUID;

public interface ReservationService {

  // 예매 확정 여부
  boolean isConfirmed(UUID reservationId);

  // 예매 정보 가져오기
  ReservationInfo getReservation(UUID reservationId);
}
