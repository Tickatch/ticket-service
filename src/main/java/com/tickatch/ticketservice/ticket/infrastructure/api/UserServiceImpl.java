package com.tickatch.ticketservice.ticket.infrastructure.api;

import com.tickatch.ticketservice.ticket.domain.service.UserService;
import com.tickatch.ticketservice.ticket.infrastructure.client.UserFeignClient;
import com.tickatch.ticketservice.ticket.domain.dto.UserInfo;
import com.tickatch.ticketservice.ticket.infrastructure.client.dto.UserClientResponse;
import io.github.tickatch.common.api.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserFeignClient userFeignClient;

  @Override
  public UserInfo getUser(UUID reserverId) {
    ApiResponse<UserClientResponse> response = userFeignClient.getRecipientInfo(reserverId);
    return response.getData().toUserInfo();
  }
}
