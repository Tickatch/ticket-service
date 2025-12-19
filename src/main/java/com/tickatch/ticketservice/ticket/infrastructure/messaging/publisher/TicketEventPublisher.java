package com.tickatch.ticketservice.ticket.infrastructure.messaging.publisher;

import com.tickatch.ticketservice.ticket.application.event.TicketIssuedEvent;
import com.tickatch.ticketservice.ticket.infrastructure.messaging.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEventPublisher {

  private final RabbitTemplate rabbitTemplate;

  public void publish(TicketIssuedEvent event) {
    rabbitTemplate.convertAndSend(RabbitMQConfig.TICKET_EXCHANGE,
        RabbitMQConfig.ROUTING_KEY_TICKET_ISSUED_NOTIFICATION, event);
  }
}