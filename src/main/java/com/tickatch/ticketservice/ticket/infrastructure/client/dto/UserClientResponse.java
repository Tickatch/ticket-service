package com.tickatch.ticketservice.ticket.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tickatch.ticketservice.ticket.domain.dto.UserInfo;
import java.util.UUID;

public record UserClientResponse(
    @JsonProperty("id")
    UUID userId,

    String email,
    String phone

    ) {

    public UserInfo toUserInfo() {
      return new UserInfo(
          userId,
          email,
          phone
      );
    }
}
