package com.tickatch.ticketservice.ticket.infrastructure.messaging.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketLogEvent(
    UUID eventId,
    UUID ticketId,
    String receiveMethod,
    String actionType,
    String actorType,
    UUID actorUserId,
    LocalDateTime occurredAt) {}
