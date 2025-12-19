package com.tickatch.ticketservice.ticket.infrastructure.client;

import com.tickatch.ticketservice.ticket.infrastructure.client.dto.ReservationClientResponse;
import io.github.tickatch.common.api.ApiResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservation-service")
public interface ReservationFeignClient {

  // 예매 확정 여부 조회
  @GetMapping("/api/v1/reservations/{reservationId}/confirmed")
  ApiResponse<Boolean> isConfirmed(@PathVariable UUID reservationId);

  // 예매 관련 정보 조회
  @GetMapping("/api/v1/reservations/{reservationId}")
  ApiResponse<ReservationClientResponse> getReservationInfo(@PathVariable UUID reservationId);
}
