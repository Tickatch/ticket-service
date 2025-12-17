package com.tickatch.ticketservice.ticket.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatInfo {

  // 예매 좌석 id
  private long seatId;

  // 좌석 등급
  @Column(nullable = false, length = 10)
  private String grade;

  // 좌석 번호
  @Column(nullable = false, length = 30)
  private String seatNumber;

  // 예매 금액
  private Long price;

  @Builder
  protected SeatInfo(long seatId, String grade, String seatNumber, Long price) {
    this.seatId = seatId;
    this.grade = grade;
    this.seatNumber = seatNumber;
    this.price = price;
  }
}
