package com.tickatch.ticketservice.ticket.infrastructure.api;

import com.tickatch.ticketservice.ticket.domain.dto.ReservationInfo;
import com.tickatch.ticketservice.ticket.domain.service.ReservationService;
import com.tickatch.ticketservice.ticket.infrastructure.client.ReservationFeignClient;
import com.tickatch.ticketservice.ticket.infrastructure.client.dto.ReservationClientResponse;
import io.github.tickatch.common.api.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

  private final ReservationFeignClient reservationFeignClient;

  @Override
  public boolean isConfirmed(UUID reservationId) {
    ApiResponse<Boolean> response = reservationFeignClient.isConfirmed(reservationId);

    Boolean confirmed = response.getData();

    return confirmed;
  }

  @Override
  public ReservationInfo getReservation(UUID reservationId) {

    ApiResponse<ReservationClientResponse> response =
        reservationFeignClient.getReservationInfo(reservationId);

    return response.getData().toReservationInfo();
  }
}
