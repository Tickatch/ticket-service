package com.tickatch.ticketservice.ticket.infrastructure.messaging.publisher;

import com.tickatch.ticketservice.ticket.application.port.TicketLogPort;
import com.tickatch.ticketservice.ticket.infrastructure.messaging.config.RabbitMQConfig;
import com.tickatch.ticketservice.ticket.infrastructure.messaging.event.TicketLogEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketLogPublisher implements TicketLogPort {

  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publishAction(
      UUID ticketId,
      String receiveMethod,
      String actionType,
      String actorType,
      UUID actorUserId,
      LocalDateTime occurredAt) {

    TicketLogEvent event =
        new TicketLogEvent(
            UUID.randomUUID(),
            ticketId,
            receiveMethod,
            actionType,
            actorType,
            actorUserId,
            occurredAt);

    rabbitTemplate.convertAndSend(
        RabbitMQConfig.LOG_EXCHANGE, RabbitMQConfig.ROUTING_KEY_TICKET_LOG, event);
  }
}
