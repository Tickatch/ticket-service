package com.tickatch.ticketservice.ticket.application.event;

import com.tickatch.ticketservice.ticket.domain.Ticket;
import com.tickatch.ticketservice.ticket.domain.TicketId;
import com.tickatch.ticketservice.ticket.domain.dto.ProductInfo;
import com.tickatch.ticketservice.ticket.domain.event.TicketIssuedDomainEvent;
import com.tickatch.ticketservice.ticket.domain.repository.TicketRepository;
import com.tickatch.ticketservice.ticket.domain.service.ProductService;
import com.tickatch.ticketservice.ticket.domain.service.ReservationService;
import com.tickatch.ticketservice.ticket.domain.service.UserService;
import com.tickatch.ticketservice.ticket.domain.dto.ReservationInfo;
import com.tickatch.ticketservice.ticket.domain.dto.UserInfo;
import com.tickatch.ticketservice.ticket.infrastructure.messaging.publisher.TicketEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketIssuedEventListener {

  private final TicketRepository ticketRepository;
  private final ReservationService reservationService;
  private final UserService userService;
  private final ProductService productService;
  private final TicketEventPublisher ticketEventPublisher;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(TicketIssuedDomainEvent event) {

    TicketId ticketId = TicketId.of(event.ticketId());

    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow();

    // 1. 예매 정보 가져오기
    ReservationInfo reservation =
        reservationService.getReservation(ticket.getReservationId());

    // 2. 예매자 id로 user 정보 가져오기
    UserInfo user =
        userService.getUser(reservation.reserverId());

    // 3. 상품 정보 가져오기
    ProductInfo product =
        productService.getProduct(ticket.getProductId());

    // 4. 이벤트 생성
    TicketIssuedEvent issuedEvent =
        TicketIssuedEvent.of(ticket, reservation, user, product);

    // 5. RabbitMQ 발행
    try {
      ticketEventPublisher.publish(issuedEvent);
    } catch (Exception e) {
      log.error("TicketIssuedEvent 발행 실패. ticketId={}", event.ticketId(), e);
    }
  }
}