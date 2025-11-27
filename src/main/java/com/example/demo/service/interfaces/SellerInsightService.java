package com.example.demo.service.interfaces;

import com.example.demo.dto.request.OrderInsightRequestDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SellerInsightService {
    Map<String, Integer> getSellerCityDistribution(OrderInsightRequestDto request); // seller distribution by city
    Map<String, Double> getMarketShare(OrderInsightRequestDto request); // sellers and their market shares
    double getHHI(OrderInsightRequestDto request); // Herfindahl–Hirschman Index: indicator of competition
    double getConcentrationRatio(OrderInsightRequestDto request); // concentration ratio: quantifier of market concentration
}
