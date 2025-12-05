package com.tickatch.ticketservice.ticket.presentation.webapi;

import com.tickatch.ticketservice.ticket.presentation.dto.CreateTicketRequest;
import io.github.tickatch.common.api.ApiResponse;
import io.github.tickatch.common.api.PageResponse;
import java.util.List;
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

  @PostMapping
  public ApiResponse<CreateTicketResponse> createTicket(@RequestBody CreateTicketRequest request) {
    // application에 있는 dto로 옮기기 작업 필요 request -> ____
    return ApiResponse.success(ticketService.createTicket(), "티켓이 발행되었습니다.");
  }

  @PostMapping("/{id}/use")
  public ApiResponse<UseTicketResponse> useTicket(@PathVariable UUID id) {
    return ApiResponse.success(ticketService.useTicket(id), "티켓이 사용되었습니다.");
  }

  @PostMapping("/{id}/cancel")
  public ApiResponse<CancelTicketResponse> cancelTicket(@PathVariable UUID id) {
    return ApiResponse.success(ticketService.cancelTicket(id), "티켓이 취소되었습니다.");
  }

  @GetMapping("/{id}")
  public ApiResponse<GetTicketResponse> getTicket(@PathVariable UUID id) {
    return ApiResponse.success(ticketService.getTicket(id), "티켓 조회를 완료했습니다.");
  }

  @GetMapping
  public PageResponse<GetTicketResponse> getAllTickets(
      @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
      Pageable pageable
  ) {
    return ticketService.getAllTickets(pageable).map(page -> PageResponse.from(page, GetTicketResponse::from));
    return ApiResponse.success(ticketService.getAllTickets(), "티켓 목록 조회를 완료했습니다.");
  }
}
