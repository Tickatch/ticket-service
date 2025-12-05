package com.tickatch.ticketservice.ticket.application.service;

import com.tickatch.ticketservice.ticket.application.dto.TicketActionDto;
import com.tickatch.ticketservice.ticket.application.dto.TicketCreateCommand;
import com.tickatch.ticketservice.ticket.application.dto.TicketDetailDto;
import com.tickatch.ticketservice.ticket.application.dto.TicketDto;
import com.tickatch.ticketservice.ticket.domain.Ticket;
import com.tickatch.ticketservice.ticket.domain.TicketId;
import com.tickatch.ticketservice.ticket.domain.exception.TicketErrorCode;
import com.tickatch.ticketservice.ticket.domain.exception.TicketException;
import com.tickatch.ticketservice.ticket.domain.repository.TicketRepository;
import com.tickatch.ticketservice.ticket.domain.service.ReservationService;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;
  private final ReservationService reservationService;

  // 1. 티켓 발행
  @Transactional
  public TicketDto createTicket(TicketCreateCommand request) {

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
            request.grade(),
            request.seatNumber(),
            request.price());

    // 5) 저장
    ticketRepository.save(newTicket);

    return TicketDto.from(newTicket);
  }

  // 2. 티켓 사용
  @Transactional
  public TicketActionDto useTicket(UUID ticketId) {
    // 티켓 id로 조회
    Ticket ticket =
        ticketRepository
            .findById(TicketId.of(ticketId))
            .orElseThrow(() -> new TicketException(TicketErrorCode.TICKET_NOT_FOUND));

    ticket.use();

    return TicketActionDto.fromUsed(ticket);
  }

  // 3. 티켓 취소
  @Transactional
  public TicketActionDto cancelTicket(UUID ticketId) {

    // 티켓 id로 조회
    Ticket ticket =
        ticketRepository
            .findById(TicketId.of(ticketId))
            .orElseThrow(() -> new TicketException(TicketErrorCode.TICKET_NOT_FOUND));

    ticket.cancel();

    return TicketActionDto.fromCanceled(ticket);
  }

  // 4. 티켓 상세 조회
  @Transactional(readOnly = true)
  public TicketDetailDto getTicketDetail(UUID ticketId) {

    // 티켓 id로 조회
    Ticket ticket =
        ticketRepository
            .findById(TicketId.of(ticketId))
            .orElseThrow(() -> new TicketException(TicketErrorCode.TICKET_NOT_FOUND));

    return TicketDetailDto.from(ticket);
  }

  // 5. 티켓 목록 조회
  @Transactional(readOnly = true)
  public Page<TicketDto> getAllTickets(Pageable pageable) {

    return ticketRepository.findAll(pageable).map(TicketDto::from);
  }
}
