package com.tickatch.ticketservice.ticket.domain.service;

import com.tickatch.ticketservice.ticket.domain.dto.ProductInfo;

public interface ProductService {

  // 상품 정보 가져오기
  ProductInfo getProduct(Long productId);
}
