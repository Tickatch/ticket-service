package com.tickatch.ticketservice.ticket.application.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tickatch.ticketservice.ticket.domain.ReceiveMethod;
import com.tickatch.ticketservice.ticket.domain.Ticket;
import com.tickatch.ticketservice.ticket.domain.dto.ProductInfo;
import com.tickatch.ticketservice.ticket.domain.dto.ReservationInfo;
import com.tickatch.ticketservice.ticket.domain.dto.UserInfo;
import io.github.tickatch.common.event.DomainEvent;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class TicketIssuedEvent extends DomainEvent {

  private final UUID ticketId;
  private final ReceiveMethod receiveMethod;
  private final UUID reservationId;
  private final String reservationNumber;
  private final UUID reserverId;
  private final String recipient;
  private final String reserverName;
  private final String productName;
  private final LocalDateTime performanceDate;
  private final String artHallName;
  private final String stageName;
  private final String seatNumber;

  public TicketIssuedEvent(
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
      String seatNumber) {
    super();
    this.ticketId = ticketId;
    this.receiveMethod = receiveMethod;
    this.reservationId = reservationId;
    this.reservationNumber = reservationNumber;
    this.reserverId = reserverId;
    this.recipient = recipient;
    this.reserverName = reserverName;
    this.productName = productName;
    this.performanceDate = performanceDate;
    this.artHallName = artHallName;
    this.stageName = stageName;
    this.seatNumber = seatNumber;
  }

  @JsonCreator
  public TicketIssuedEvent(
      @JsonProperty("eventId") String eventId,
      @JsonProperty("occurredAt") Instant occurredAt,
      @JsonProperty("version") int version,
      @JsonProperty("ticketId") UUID ticketId,
      @JsonProperty("receiveMethod") ReceiveMethod receiveMethod,
      @JsonProperty("reservationId") UUID reservationId,
      @JsonProperty("reservationNumber") String reservationNumber,
      @JsonProperty("reserverId") UUID reserverId,
      @JsonProperty("recipient") String recipient,
      @JsonProperty("reserverName") String reserverName,
      @JsonProperty("productName") String productName,
      @JsonProperty("performanceDate") LocalDateTime performanceDate,
      @JsonProperty("artHallName") String artHallName,
      @JsonProperty("stageName") String stageName,
      @JsonProperty("seatNumber") String seatNumber) {
    super(eventId, occurredAt, version);
    this.ticketId = ticketId;
    this.receiveMethod = receiveMethod;
    this.reservationId = reservationId;
    this.reserverId = reserverId;
    this.reservationNumber = reservationNumber;
    this.recipient = recipient;
    this.reserverName = reserverName;
    this.productName = productName;
    this.performanceDate = performanceDate;
    this.artHallName = artHallName;
    this.stageName = stageName;
    this.seatNumber = seatNumber;
  }

  public static TicketIssuedEvent of(
      Ticket ticket, ReservationInfo reservation, UserInfo user, ProductInfo product) {
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
        ticket.getSeatInfo().getSeatNumber());
  }

  private static String resolveRecipient(ReceiveMethod receiveMethod, UserInfo user) {
    return switch (receiveMethod) {
      case EMAIL -> user.email();
      case MMS -> user.phone();
      default -> throw new IllegalStateException("알림 대상이 아닌 수령 방식: " + receiveMethod);
    };
  }
}
