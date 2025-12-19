package com.tickatch.ticketservice.ticket.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tickatch.ticketservice.ticket.domain.dto.ProductInfo;
import java.time.LocalDateTime;

public record ProductClientResponse(
    @JsonProperty("id")
    Long productId,

    @JsonProperty("name")
    String productName,

    @JsonProperty("startAt")
    LocalDateTime performanceDate,

    String artHallName,
    String stageName
) {

  public ProductInfo toProductInfo() {
    return new ProductInfo(
        productId,
        productName,
        performanceDate,
        artHallName,
        stageName
    );
  }
}