package com.tickatch.ticketservice.ticket.presentation.webapi;

import com.tickatch.ticketservice.ticket.application.dto.TicketActionResponse;
import com.tickatch.ticketservice.ticket.application.dto.TicketDetailResponse;
import com.tickatch.ticketservice.ticket.application.dto.TicketRequest;
import com.tickatch.ticketservice.ticket.application.dto.TicketResponse;
import com.tickatch.ticketservice.ticket.application.service.TicketService;
import com.tickatch.ticketservice.ticket.presentation.dto.CreateTicketRequest;
import io.github.tickatch.common.api.ApiResponse;
import io.github.tickatch.common.api.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketApi {

  private final TicketService ticketService;

  // 1. 티켓 생성
  @PostMapping
  @Operation(summary = "새로운 티켓 생성", description = "새로운 티켓을 생성합니다.")
  public ApiResponse<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
    TicketRequest ticketRequest = request.toTicketRequest();
    return ApiResponse.success(ticketService.createTicket(ticketRequest), "티켓이 발행되었습니다.");
  }

  // 2. 티켓 사용
  @PostMapping("/{id}/use")
  @Operation(summary = "티켓 사용", description = "해당 id의 티켓을 사용합니다.")
  public ApiResponse<TicketActionResponse> useTicket(@PathVariable UUID id) {
    return ApiResponse.success(ticketService.useTicket(id), "티켓이 사용되었습니다.");
  }

  // 3. 티켓 취소
  @PostMapping("/{id}/cancel")
  @Operation(summary = "티켓 취소", description = "해당 id의 티켓을 취소합니다.")
  public ApiResponse<TicketActionResponse> cancelTicket(@PathVariable UUID id) {
    return ApiResponse.success(ticketService.cancelTicket(id), "티켓이 취소되었습니다.");
  }

  // 4. 티켓 상세 조회
  @GetMapping("/{id}")
  @Operation(summary = "티켓 상세 조회", description = "하나의 티켓의 상세 정보를 조회합니다.")
  public ApiResponse<TicketDetailResponse> getTicketDetail(@PathVariable UUID id) {
    return ApiResponse.success(ticketService.getTicketDetail(id), "티켓 조회를 완료했습니다.");
  }

  // 5. 티켓 목록 조회
  @GetMapping
  @Operation(summary = "티켓 목록 조회", description = "사용자가 소유한 티켓 전체를 조회합니다.")
  public PageResponse<TicketResponse> getAllTickets(
      @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return PageResponse.from(ticketService.getAllTickets(pageable));
  }

  // 6. 예매 취소에 따른 티켓 취소
  @PostMapping("/{reservationId}/cancel")
  @Operation(summary = "예매 취소로 인한 티켓 취소", description = "예매가 취소되어 해당 예매의 티켓을 취소합니다.")
  public ApiResponse<Void> cancelByReservationId(@PathVariable UUID reservationId) {
    ticketService.cancelTicket(reservationId);
    return ApiResponse.success();
  }
}
