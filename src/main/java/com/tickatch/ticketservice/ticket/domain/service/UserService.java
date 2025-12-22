package com.tickatch.ticketservice.ticket.domain.service;

import com.tickatch.ticketservice.ticket.domain.dto.UserInfo;
import java.util.UUID;

public interface UserService {

  UserInfo getUser(UUID reserverId);
}
