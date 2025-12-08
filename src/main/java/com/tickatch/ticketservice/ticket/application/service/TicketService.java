package com.tickatch.ticketservice.ticket.application.service;

import com.tickatch.ticketservice.ticket.application.dto.TicketActionResponse;
import com.tickatch.ticketservice.ticket.application.dto.TicketDetailResponse;
import com.tickatch.ticketservice.ticket.application.dto.TicketRequest;
import com.tickatch.ticketservice.ticket.application.dto.TicketResponse;
import com.tickatch.ticketservice.ticket.domain.Ticket;
import com.tickatch.ticketservice.ticket.domain.TicketId;
import com.tickatch.ticketservice.ticket.domain.exception.TicketErrorCode;
import com.tickatch.ticketservice.ticket.domain.exception.TicketException;
import com.tickatch.ticketservice.ticket.domain.repository.TicketRepository;
import com.tickatch.ticketservice.ticket.domain.service.ReservationService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

  private final TicketRepository ticketRepository;
  private final ReservationService reservationService;

  // 1. 티켓 발행
  @Transactional
  public TicketResponse createTicket(TicketRequest request) {

    // 1) 예매 확정 여부 확인
    if (!reservationService.isConfirmed(request.reservationId())) {
      throw new TicketException(TicketErrorCode.RESERVATION_NOT_FOUND);
    }

    // 2) 기존 티켓 조회
    Optional<Ticket> existingTicket = ticketRepository.findByReservationId(request.reservationId());

    // 3) 기존 티켓이 발행된 상태이면 기존 티켓을 취소처리
    existingTicket.ifPresent(
        t -> {
          // 이미 사용된 경우
          if (t.isUsed()) {
            throw new TicketException(TicketErrorCode.ALREADY_USED);
          }

          if (t.isIssued()) {
            t.cancel();
          }
        });

    // 4) 새 티켓 발행
    Ticket newTicket =
        Ticket.issue(
            request.reservationId(),
            request.seatId(),
            request.productId(),
            request.grade(),
            request.seatNumber(),
            request.price(),
            request.receiveMethod());

    // 5) 저장
    ticketRepository.save(newTicket);

    return TicketResponse.from(newTicket);
  }

  // 2. 티켓 사용
  @Transactional
  public TicketActionResponse useTicket(UUID ticketId) {
    // 티켓 id로 조회
    Ticket ticket =
        ticketRepository
            .findById(TicketId.of(ticketId))
            .orElseThrow(() -> new TicketException(TicketErrorCode.TICKET_NOT_FOUND));

    ticket.use();

    return TicketActionResponse.fromUsed(ticket);
  }

  // 3. 티켓 취소
  @Transactional
  public TicketActionResponse cancelTicket(UUID ticketId) {

    // 티켓 id로 조회
    Ticket ticket =
        ticketRepository
            .findById(TicketId.of(ticketId))
            .orElseThrow(() -> new TicketException(TicketErrorCode.TICKET_NOT_FOUND));

    ticket.cancel();

    return TicketActionResponse.fromCanceled(ticket);
  }

  // 4. 티켓 상세 조회
  @Transactional(readOnly = true)
  public TicketDetailResponse getTicketDetail(UUID ticketId) {

    // 티켓 id로 조회
    Ticket ticket =
        ticketRepository
            .findById(TicketId.of(ticketId))
            .orElseThrow(() -> new TicketException(TicketErrorCode.TICKET_NOT_FOUND));

    return TicketDetailResponse.from(ticket);
  }

  // 5. 티켓 목록 조회
  @Transactional(readOnly = true)
  public Page<TicketResponse> getAllTickets(Pageable pageable) {

    return ticketRepository.findAll(pageable).map(TicketResponse::from);
  }

  // 6. 상품 취소 이벤트 처리
  @Transactional
  public void invalidateByProductId(Long productId) {

    List<Ticket> tickets = ticketRepository.findAllByProductId(productId);

    if (tickets.isEmpty()) {
      log.info("취소할 티켓 없음. productId={}", productId);
      return;
    }

    int cancelledCount = 0;
    for (Ticket ticket : tickets) {
      try {
        ticket.cancel();
        cancelledCount++;
      } catch (TicketException e) {
        log.warn("이미 사용된 티켓, 취소 불가. ticketId={}, reason={}", ticket.getId(), e.getMessage());
      }
    }

    log.info("총 {}건의 티켓 취소 완료. productId={}", cancelledCount, productId);
  }

  // 7. 예매 id로 티켓 취소
  @Transactional
  public void cancelByReservationID(UUID reservationId) {

    Ticket ticket =
        ticketRepository
            .findByReservationId(reservationId)
            .orElseThrow(() -> new TicketException(TicketErrorCode.TICKET_NOT_FOUND));

    try {
      ticket.cancel();
    } catch (Exception e) {
      log.warn("예매 id가 reservationId={}인 티켓 취소 실패", reservationId);
    }

    log.info("예매 id가 reservationId={}인 티켓 취소 성공", reservationId);
  }
}
