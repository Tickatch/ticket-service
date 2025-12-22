package com.tickatch.ticketservice.ticket.application.port;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TicketLogPort {

  void publishAction(
      UUID ticketId,
      String receiveMethod,
      String actionType,
      String actorType,
      UUID actorUserId,
      LocalDateTime occurredAt);
}
