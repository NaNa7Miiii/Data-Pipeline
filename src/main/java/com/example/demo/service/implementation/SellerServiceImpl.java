package com.example.demo.service.implementation;

import com.example.demo.dto.request.OrderInsightRequestDto;
import com.example.demo.repository.SellerRepository;
import com.example.demo.service.interfaces.OrderCityView;
import com.example.demo.service.interfaces.SellerInsightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SellerServiceImpl implements SellerInsightService {
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    // returns the geographic distribution of sellers by cities
    public Map<String, Integer> getSellerCityDistribution(OrderInsightRequestDto request) {
        return calculateCityDistribution(loadSellers(request));
    }

    @Override
    // returns a map of seller id and its market share
    public Map<String, Double> getMarketShare(OrderInsightRequestDto request) {
        return calculateMarketShare(loadSellers(request));
    }

    @Override
    // returns the Herfindahl–Hirschman Index (HHI),
    // a measure of the size of firms in relation to the industry they are in, an indicator of competition,
    // sum[(market share of each seller)^2]
    public double getHHI(OrderInsightRequestDto request) {
        return calculateMarketShare(loadSellers(request)).values()
                .stream()
                .mapToDouble(v -> v * v)
                .sum();
    }

    @Override
    // returns the concentration ratio,
    // a quantifier of market concentration based on company market shares,
    // sum(market share of each seller)
    public double getConcentrationRatio(OrderInsightRequestDto request) {
        return calculateMarketShare(loadSellers(request)).values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private List<OrderCityView> loadSellers(OrderInsightRequestDto request) {
        LocalDateTime start = request.startDate();
        LocalDateTime end = request.endDate();
        String city = request.city();
        String category = request.category();
        return sellerRepository.findSellersByFilters(start, end, city, category);
    }

    private Map<String, Double> calculateMarketShare(List<OrderCityView> orders) {
        double marketValue = (double) orders.stream()
                .mapToDouble(o -> o.getPrice() == null ? 0.0 : o.getPrice())
                .sum();
        Map<String, Double> sellerRevenue = orders.stream()
                .collect(Collectors.groupingBy(OrderCityView::getSellerId,
                        Collectors.summingDouble(OrderCityView::getPrice)));
        Map<String, Double> sellerShare = sellerRevenue.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                e -> e.getValue() / marketValue));
        return sellerShare;
    }

    private Map<String, Integer> calculateCityDistribution(List<OrderCityView> orders) {
        Map<String, Integer> distribution = new HashMap<>();
        for (OrderCityView order : orders) {
            String city = order.getSellerCity();
            if (city == null) continue;
            distribution.put(city, distribution.getOrDefault(city, 0) + 1);
        }
        return distribution;
    }
}
