package com.tickatch.ticketservice.ticket.infrastructure.api;

import com.tickatch.ticketservice.ticket.domain.dto.ProductInfo;
import com.tickatch.ticketservice.ticket.domain.service.ProductService;
import com.tickatch.ticketservice.ticket.infrastructure.client.ProductFeignClient;
import com.tickatch.ticketservice.ticket.infrastructure.client.dto.ProductClientResponse;
import io.github.tickatch.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductFeignClient productFeignClient;

  @Override
  public ProductInfo getProduct(Long productId) {
    ApiResponse<ProductClientResponse> response = productFeignClient.getProductInfo(productId);

    ProductClientResponse data = response.getData();

    return data.toProductInfo();
  }
}
