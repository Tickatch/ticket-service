package com.tickatch.ticketservice.ticket.infrastructure.messaging.config;

import io.github.tickatch.common.util.JsonUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Value("${messaging.exchange.product:tickatch.product}")
  private String productExchange;

  /** Product 서비스에서 발행하는 Ticket 취소 이벤트 큐 */
  public static final String QUEUE_PRODUCT_CANCELLED_TICKET =
      "tickatch.product.cancelled.ticket.queue";

  /** Ticket 라우팅 키 */
  public static final String ROUTING_KEY_CANCELLED_TICKET = "product.cancelled.ticket";

  /** Log 서비스 Exchange (공통 로그용) */
  public static final String LOG_EXCHANGE = "tickatch.log";

  /** Ticket 로그 라우팅 키 */
  public static final String ROUTING_KEY_TICKET_LOG = "ticket.log";

  // ==================================================
  // Ticket → Notification
  // ==================================================
  public static final String TICKET_EXCHANGE = "tickatch.ticket";
  public static final String QUEUE_TICKET_ISSUED_NOTIFICATION =
      "tickatch.ticket.issued.notification.queue";
  public static final String ROUTING_KEY_TICKET_ISSUED_NOTIFICATION =
      "ticket.issued.notification";


  // ========================================
  // Exchange (Consumer도 선언 필요 - 멱등성 보장)
  // ========================================
  // 상품
  @Bean
  public TopicExchange productExchange() {
    return ExchangeBuilder.topicExchange(productExchange).durable(true).build();
  }

  // 로그
  @Bean
  public TopicExchange logExchange() {
    return ExchangeBuilder.topicExchange(LOG_EXCHANGE).durable(true).build();
  }

  // 티켓에서 알림
  @Bean
  public TopicExchange ticketExchange() {
    return ExchangeBuilder.topicExchange(TICKET_EXCHANGE).durable(true).build();
  }


  // ========================================
  // Queues
  // ========================================
  @Bean
  public Queue productCancelledTicketQueue() {
    return QueueBuilder.durable(QUEUE_PRODUCT_CANCELLED_TICKET)
        .withArgument("x-dead-letter-exchange", productExchange + ".dlx")
        .withArgument("x-dead-letter-routing-key", "dlq." + ROUTING_KEY_CANCELLED_TICKET)
        .build();
  }

  /** Ticket 발급 → 알림용 큐 */
  @Bean
  public Queue ticketIssuedNotificationQueue() {
    return QueueBuilder.durable(QUEUE_TICKET_ISSUED_NOTIFICATION)
        .withArgument("x-dead-letter-exchange", TICKET_EXCHANGE + ".dlx")
        .withArgument(
            "x-dead-letter-routing-key",
            "dlq." + ROUTING_KEY_TICKET_ISSUED_NOTIFICATION)
        .build();
  }

  // ========================================
  // Bindings
  // ========================================
  @Bean
  public Binding productCancelledTicketBinding(
      Queue productCancelledTicketQueue, TopicExchange productExchange) {
    return BindingBuilder.bind(productCancelledTicketQueue)
        .to(productExchange)
        .with(ROUTING_KEY_CANCELLED_TICKET);
  }

  /** TicketIssued 바인딩 */
  @Bean
  public Binding ticketIssuedNotificationBinding(
      Queue ticketIssuedNotificationQueue,
      TopicExchange ticketExchange
  ) {
    return BindingBuilder.bind(ticketIssuedNotificationQueue)
        .to(ticketExchange)
        .with(ROUTING_KEY_TICKET_ISSUED_NOTIFICATION);
  }

  // ========================================
  // Dead Letter Exchange & Queues
  // ========================================
  @Bean
  public TopicExchange deadLetterExchange() {
    return ExchangeBuilder.topicExchange(productExchange + ".dlx").durable(true).build();
  }

  @Bean
  public Queue deadLetterTicketQueue() {
    return QueueBuilder.durable(QUEUE_PRODUCT_CANCELLED_TICKET + ".dlq").build();
  }

  @Bean
  public Binding deadLetterTicketBinding(
      Queue deadLetterTicketQueue, TopicExchange deadLetterExchange) {
    return BindingBuilder.bind(deadLetterTicketQueue)
        .to(deadLetterExchange)
        .with("dlq." + ROUTING_KEY_CANCELLED_TICKET);
  }

  // ========================================
  // Dead Letter (Ticket)
  // ========================================
  @Bean
  public TopicExchange ticketDeadLetterExchange() {
    return ExchangeBuilder
        .topicExchange(TICKET_EXCHANGE + ".dlx")
        .durable(true)
        .build();
  }

  @Bean
  public Queue ticketIssuedNotificationDlq() {
    return QueueBuilder
        .durable(QUEUE_TICKET_ISSUED_NOTIFICATION + ".dlq")
        .build();
  }

  @Bean
  public Binding ticketIssuedNotificationDlqBinding(
      Queue ticketIssuedNotificationDlq,
      TopicExchange ticketDeadLetterExchange
  ) {
    return BindingBuilder.bind(ticketIssuedNotificationDlq)
        .to(ticketDeadLetterExchange)
        .with("dlq." + ROUTING_KEY_TICKET_ISSUED_NOTIFICATION);
  }

  // ========================================
  // Message Converter & Listener Factory
  // ========================================
  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter(JsonUtils.getObjectMapper());
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonMessageConverter);
    return rabbitTemplate;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(jsonMessageConverter);
    factory.setDefaultRequeueRejected(false); // 실패 시 DLQ로 이동
    factory.setPrefetchCount(10);
    return factory;
  }
}
