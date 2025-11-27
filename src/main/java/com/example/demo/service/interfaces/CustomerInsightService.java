package com.example.demo.service.interfaces;

import com.example.demo.dto.request.OrderInsightRequestDto;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CustomerInsightService {
    int getCustomerCount(OrderInsightRequestDto request); // unique number of customers
    double getAvgCLV(OrderInsightRequestDto request); // average customer lifetime value
    Map<String, Integer> getCustomerCityDistribution(OrderInsightRequestDto request); // customer distribution by city
    double getRepeatRate(OrderInsightRequestDto request); //customer repeat rate

//    int getNewCustomerCount(OrderInsightRequestDto request); // customer count in filter criteria with no previous purchases
//    double getAvgPurchaseInterval(OrderInsightRequestDto request); // average purchase interval for returning customers
}
