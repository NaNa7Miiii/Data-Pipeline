package com.example.demo.service.interfaces;

import com.example.demo.dto.request.OrderInsightRequestDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface OrderInsightService {
    // separate methods for agent to use
    double getGmv(OrderInsightRequestDto request);
    int getOrderCount(OrderInsightRequestDto request);
    double getAvgOrderValue(OrderInsightRequestDto request);
    double getRepeatRate(OrderInsightRequestDto request);
    double getAvgFulfillmentHours(OrderInsightRequestDto request);
    Map<String, Integer> getCityDistribution(OrderInsightRequestDto request);
    Map<String, Integer> getCategoryDistribution(OrderInsightRequestDto request);
    String getOrderStatus(String orderId);
    Map<String, Double> getTopSellers(OrderInsightRequestDto request);
}