package com.tickatch.ticketservice.ticket.domain.dto;

import java.time.LocalDateTime;

public record ProductInfo(
    Long productId, // product.id
    String productName, // product.name
    LocalDateTime performanceDate, // product.startAt
    String artHallName,
    String stageName) {}
