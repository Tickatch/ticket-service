package com.tickatch.ticketservice.ticket.application.port;

import com.tickatch.ticketservice.ticket.application.event.TicketIssuedEvent;

public interface TicketEventPublisherPort {
  void publish(TicketIssuedEvent event);
}
