package com.tickatch.ticketservice.ticket.infrastructure.messaging.publisher;

import com.tickatch.ticketservice.ticket.application.event.TicketIssuedEvent;
import com.tickatch.ticketservice.ticket.application.port.TicketEventPublisherPort;
import com.tickatch.ticketservice.ticket.infrastructure.messaging.config.RabbitMQConfig;
import io.github.tickatch.common.event.IntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEventPublisher implements TicketEventPublisherPort {

  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(TicketIssuedEvent event) {
    IntegrationEvent integrationEvent = IntegrationEvent.from(event, "ticket-service");
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.TICKET_EXCHANGE,
        RabbitMQConfig.ROUTING_KEY_TICKET_ISSUED_NOTIFICATION,
        integrationEvent);
  }
}
