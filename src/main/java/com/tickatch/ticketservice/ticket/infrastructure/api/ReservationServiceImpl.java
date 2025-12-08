package com.tickatch.ticketservice.ticket.infrastructure.api;

import com.tickatch.ticketservice.ticket.domain.exception.TicketErrorCode;
import com.tickatch.ticketservice.ticket.domain.exception.TicketException;
import com.tickatch.ticketservice.ticket.domain.service.ReservationService;
import com.tickatch.ticketservice.ticket.infrastructure.client.ReservationFeignClient;
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

    if (response == null) {
      throw new TicketException(TicketErrorCode.RESERVATION_SERVICE_ERROR);
    }

    Boolean confirmed = response.getData();

    if (confirmed == null) {
      throw new TicketException(TicketErrorCode.RESERVATION_SERVICE_INVALID_RESPONSE);
    }

    return confirmed;
  }
}
