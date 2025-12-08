package com.tickatch.ticketservice.ticket.domain;

import com.tickatch.ticketservice.global.domain.AbstractAuditEntity;
import com.tickatch.ticketservice.ticket.domain.exception.TicketErrorCode;
import com.tickatch.ticketservice.ticket.domain.exception.TicketException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "p_ticket")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends AbstractAuditEntity {

  // 티켓 id
  @EmbeddedId private TicketId id;

  // 예매 id
  @Column(nullable = false)
  private UUID reservationId;

  // 좌석 정보
  @Embedded private SeatInfo seatInfo;

  // 티켓 수령 방법
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReceiveMethod receiveMethod;

  // 티켓 상태
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TicketStatus status;

  // 티켓 사용 시간
  private LocalDateTime usedAt;

  // ==================================

  // 생성

  // 1. 티켓 생성
  @Builder(access = AccessLevel.PRIVATE)
  public Ticket(
      UUID ticketId,
      UUID reservationId,
      long seatId,
      String grade,
      String seatNumber,
      Long price,
      String receiveMethod) {
    this.id = TicketId.of(ticketId);
    this.reservationId = Objects.requireNonNull(reservationId, "ReservationId cannot be null");
    this.seatInfo =
        SeatInfo.builder().seatId(seatId).grade(grade).seatNumber(seatNumber).price(price).build();

    this.status = TicketStatus.ISSUED;
    this.usedAt = null;
  }

  // 팩토리 메서드
  public static Ticket issue(
      UUID reservationId,
      long seatId,
      String grade,
      String seatNumber,
      Long price,
      String receiveMethod) {
    return Ticket.builder()
        .reservationId(reservationId)
        .seatId(seatId)
        .grade(grade)
        .seatNumber(seatNumber)
        .price(price)
        .receiveMethod(receiveMethod)
        .build();
  }

  // ==================================

  // 상태 관련

  // 1. 티켓 사용
  public void use() {
    if (this.status != TicketStatus.ISSUED) {
      throw new TicketException(TicketErrorCode.INVALID_STATUS_FOR_USE);
    }
    this.status = TicketStatus.USED;
    this.usedAt = LocalDateTime.now();
  }

  // 2. 티켓 취소
  public void cancel() {
    if (this.status == TicketStatus.USED) {
      throw new TicketException(TicketErrorCode.ALREADY_USED);
    }
    this.status = TicketStatus.CANCELED;
  }

  // 3. 발행 여부 확인
  public boolean isIssued() {
    return this.status == TicketStatus.ISSUED;
  }

  // 4. 티켓 사용 여부 확인
  public boolean isUsed() {
    return this.status == TicketStatus.USED;
  }
}
