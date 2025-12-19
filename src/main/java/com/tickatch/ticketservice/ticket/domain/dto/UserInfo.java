package com.tickatch.ticketservice.ticket.domain.dto;

import java.util.UUID;

public record UserInfo(
    UUID reserverId,
    String email,
    String phone
) {

}
