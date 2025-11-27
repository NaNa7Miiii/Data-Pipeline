package com.example.demo.service.interfaces;

import com.example.demo.dto.request.OrderInsightRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OrderInsightService {
    // separate methods for agent to use
    double getGMV(OrderInsightRequestDto request); // Gross Merchandise Value
    int getOrderCount(OrderInsightRequestDto request); // number of orders
    double getAvgOrderValue(OrderInsightRequestDto request); // average order count
    double getAvgFulfillmentHours(OrderInsightRequestDto request); // average fulfillment hours (purchase to delivered)
    Map<String, Integer> getCategoryDistribution(OrderInsightRequestDto request); // order distribution by categories
    double getDeliverySLAComplianceRate(OrderInsightRequestDto request); // rate of on time deliveries
    double getAvgCarrierPickupHours(OrderInsightRequestDto request); // average number of hours for carrier to pickup
    double getCancelRate(OrderInsightRequestDto request); // rate of orders cancelled
    double getLowReviewScoreRate(OrderInsightRequestDto request); // rate of orders with review score < 3
    String getLowestAvgReviewCategory(OrderInsightRequestDto request); // category with the lowest review score
    String getHighestAvgReviewCategory(OrderInsightRequestDto request); // category with the highest review score
}