package com.tickatch.ticketservice.ticket.domain.event;

import java.util.UUID;

public record TicketIssuedDomainEvent(UUID ticketId) {}
