package com.finalproject.dataplatform.service.interfaces;

import com.finalproject.dataplatform.dto.requests.OrderInsightRequestDto;
import com.finalproject.dataplatform.dto.responses.OrderInsightResponseDto;

public interface OrderInsightService {
    OrderInsightResponseDto getOrderInsights(OrderInsightRequestDto request);
}
