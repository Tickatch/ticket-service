package com.tickatch.ticketservice.ticket.presentation.webapi;

import com.tickatch.ticketservice.ticket.application.dto.TicketActionResponse;
import com.tickatch.ticketservice.ticket.application.dto.TicketDetailResponse;
import com.tickatch.ticketservice.ticket.application.dto.TicketRequest;
import com.tickatch.ticketservice.ticket.application.dto.TicketResponse;
import com.tickatch.ticketservice.ticket.application.service.TicketService;
import com.tickatch.ticketservice.ticket.presentation.dto.CreateTicketRequest;
import io.github.tickatch.common.api.ApiResponse;
import io.github.tickatch.common.api.PageResponse;
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

  @PostMapping
  public ApiResponse<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
    TicketRequest ticketRequest = request.toTicketRequest();
    return ApiResponse.success(ticketService.createTicket(ticketRequest), "티켓이 발행되었습니다.");
  }

  @PostMapping("/{id}/use")
  public ApiResponse<TicketActionResponse> useTicket(@PathVariable UUID id) {
    return ApiResponse.success(ticketService.useTicket(id), "티켓이 사용되었습니다.");
  }

  @PostMapping("/{id}/cancel")
  public ApiResponse<TicketActionResponse> cancelTicket(@PathVariable UUID id) {
    return ApiResponse.success(ticketService.cancelTicket(id), "티켓이 취소되었습니다.");
  }

  @GetMapping("/{id}")
  public ApiResponse<TicketDetailResponse> getTicketDetail(@PathVariable UUID id) {
    return ApiResponse.success(ticketService.getTicketDetail(id), "티켓 조회를 완료했습니다.");
  }

  @GetMapping
  public PageResponse<TicketResponse> getAllTickets(
      @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return PageResponse.from(ticketService.getAllTickets(pageable));
  }
}
