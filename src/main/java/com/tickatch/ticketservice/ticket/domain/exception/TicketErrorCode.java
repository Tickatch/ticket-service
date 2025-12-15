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
  TICKET_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "TICKET_NOT_FOUND"),
  INVALID_STATUS_FOR_CANCEL(HttpStatus.BAD_REQUEST.value(), "INVALID_STATUS_FOR_CANCEL"),

  RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "RESERVATION_NOT_FOUND"),
  RESERVATION_NOT_CONFIRMED(HttpStatus.BAD_REQUEST.value(), "RESERVATION_NOT_CONFIRMED"),

  RESERVATION_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "RESERVATION_SERVICE_ERROR"),
  RESERVATION_SERVICE_INVALID_RESPONSE(
      HttpStatus.BAD_REQUEST.value(), "RESERVATION_SERVICE_INVALID_RESPONSE"),
  ;

  private final int status;
  private final String code;
}
