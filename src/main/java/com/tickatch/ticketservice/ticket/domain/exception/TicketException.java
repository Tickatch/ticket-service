package com.tickatch.ticketservice.ticket.domain.exception;

import io.github.tickatch.common.error.BusinessException;
import io.github.tickatch.common.error.ErrorCode;

public class TicketException extends BusinessException {

  public TicketException(ErrorCode errorCode) {
    super(errorCode);
  }

  public TicketException(ErrorCode errorCode, Object... errorArgs) {
    super(errorCode, errorArgs);
  }

  public TicketException(ErrorCode errorCode, Throwable cause,
      Object... errorArgs) {
    super(errorCode, cause, errorArgs);
  }
}
