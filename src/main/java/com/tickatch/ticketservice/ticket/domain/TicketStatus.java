package com.tickatch.ticketservice.ticket.domain;

public enum TicketStatus {
  ISSUED, // 발행됨
  USED, // 사용됨
  CANCELED, // 취소됨
  EXPIRED // 만료됨
}
