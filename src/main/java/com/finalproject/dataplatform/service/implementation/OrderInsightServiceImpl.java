package com.finalproject.dataplatform.service.implementation;

import com.finalproject.dataplatform.dto.requests.OrderInsightRequestDto;
import com.finalproject.dataplatform.dto.responses.OrderInsightResponseDto;
import com.finalproject.dataplatform.model.Order;
import com.finalproject.dataplatform.repository.OrderRepository;
import com.finalproject.dataplatform.service.interfaces.OrderCityView;
import com.finalproject.dataplatform.service.interfaces.OrderInsightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderInsightServiceImpl implements OrderInsightService {
    @Autowired
    public OrderRepository orderRepository;

    @Override
    public OrderInsightResponseDto getOrderInsights(OrderInsightRequestDto request) {
        // 1. Construct the constraints for query
        LocalDateTime start = request.startDate();
        LocalDateTime end = request.endDate();
        String city = request.city();
        String category = request.category();

        // 2. Query the order data
        List<OrderCityView> orders = orderRepository.findOrdersByFilters(start, end, city, category);

        // 3. Compute the insights
        double gmv = orders.stream()
                .mapToDouble(o -> o.getPrice() == null ? 0.0 : o.getPrice())
                .sum();
        int orderCount = orders.size();
        double avgOrderValue = orderCount == 0 ? 0 : gmv / orderCount;
        double repeatRate = calculateRepeatRate(orders);
        double avgFulfillmentTime = calculateAvgFulfillmentTime(orders);
        Map<String, Integer> cityDistribution = calculateCityDistribution(orders);
        Map<String, Integer> categoryDistribution = calculateCategoryDistribution(orders);

        return new OrderInsightResponseDto(
                gmv,
                orderCount,
                avgOrderValue,
                repeatRate,
                avgFulfillmentTime,
                cityDistribution,
                categoryDistribution
        );
    }

    // function to calculate repeat rate (should be implemented via PRD file)
    private double calculateRepeatRate(List<OrderCityView> orders) {
        if (orders.isEmpty()) return 0.0;
        Map<String, Integer> customerOrderCounter = new HashMap<>();
        for (OrderCityView order: orders) {
            customerOrderCounter.put(order.getCustomerId(),
                                     customerOrderCounter.getOrDefault(order.getCustomerId(), 0) + 1);
        }
        long repeatCustomers = customerOrderCounter.values().stream()
                .filter(count -> count > 1)
                .count();
        int totalCustomers = customerOrderCounter.size();
        return (double) repeatCustomers / totalCustomers;
    }

    // function to calculate average fulfillment time (should be implemented via PRD file)
    // average fulfillment time = avg([orderDeliveredCustomerDate - orderPurchaseTimestamp])
    private double calculateAvgFulfillmentTime(List<OrderCityView> orders) {
        OptionalDouble averageHours = orders.stream()
                .filter(order -> order.getOrderDeliveredCustomerDate() != null && order.getOrderPurchaseTimestamp() != null)
                .mapToLong(order -> java.time.Duration.between(
                        order.getOrderPurchaseTimestamp(),
                        order.getOrderDeliveredCustomerDate()).toHours())
                .average();
        return averageHours.orElse(0.0);
    }

    // function to calculate city distribution (should be implemented via PRD file)
    private Map<String, Integer> calculateCityDistribution(List<OrderCityView> orders) {
        Map<String, Integer> distribution = new HashMap<>();
        for (OrderCityView order : orders) {
            String city = order.getCustomerCity();
            if (city == null) continue;
            distribution.put(city, distribution.getOrDefault(city, 0) + 1);
        }
        return distribution;
    }

    // function to calculate category distribution (should be implemented via PRD file)
    private Map<String, Integer> calculateCategoryDistribution(List<OrderCityView> orders) {
        Map<String, Integer> distribution = new HashMap<>();
        for (OrderCityView order : orders) {
            String category = order.getProductCategoryNameEnglish();
            if (category == null) continue;
            distribution.put(category, distribution.getOrDefault(category, 0) + 1);
        }
        return distribution;
    }
}
