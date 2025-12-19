package com.tickatch.ticketservice.ticket.application.event;

import com.tickatch.ticketservice.ticket.domain.ReceiveMethod;
import com.tickatch.ticketservice.ticket.domain.Ticket;
import com.tickatch.ticketservice.ticket.domain.dto.ProductInfo;
import com.tickatch.ticketservice.ticket.domain.dto.ReservationInfo;
import com.tickatch.ticketservice.ticket.domain.dto.UserInfo;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketIssuedEvent(
    UUID ticketId,
    ReceiveMethod receiveMethod,
    UUID reservationId,
    String reservationNumber,
    UUID reserverId,
    String recipient,
    String reserverName,
    String productName,
    LocalDateTime performanceDate,
    String artHallName,
    String stageName,
    String seatNumber
) {
  public static TicketIssuedEvent of(
      Ticket ticket,
      ReservationInfo reservation,
      UserInfo user,
      ProductInfo product
  ) {
    String recipient = resolveRecipient(ticket.getReceiveMethod(), user);

    return new TicketIssuedEvent(
        ticket.getId().toUuid(),
        ticket.getReceiveMethod(),
        reservation.reservationId(),
        reservation.reservationNumber(),
        reservation.reserverId(),
        recipient,
        reservation.reserverName(),
        product.productName(),
        product.performanceDate(),
        product.artHallName(),
        product.stageName(),
        ticket.getSeatInfo().getSeatNumber()
    );
  }

  private static String resolveRecipient(
      ReceiveMethod receiveMethod,
      UserInfo user
  ) {
    return switch (receiveMethod) {
      case EMAIL -> user.email();
      case MMS -> user.phone();
      default ->
          throw new IllegalStateException("알림 대상이 아닌 수령 방식: " + receiveMethod);
    };
  }
}
