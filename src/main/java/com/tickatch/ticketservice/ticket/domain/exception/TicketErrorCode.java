package com.tickatch.ticketservice.ticket.domain.exception;

import io.github.tickatch.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TicketErrorCode implements ErrorCode {
  INVALID_STATUS_FOR_USE(HttpStatus.BAD_REQUEST.value(), "INVALID_STATUS_FOR_USE"),
  ALREADY_USED(HttpStatus.CONFLICT.value(), "ALREADY_USED"),

  ;


  private final int status;
  private final String code;
}
