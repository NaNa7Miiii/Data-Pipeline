package com.example.demo.service.implementation;

import com.example.demo.dto.request.OrderInsightRequestDto;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.interfaces.OrderCityView;
import com.example.demo.service.interfaces.OrderInsightService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

@Service
public class OrderInsightServiceImpl implements OrderInsightService {
    @Autowired
    public OrderRepository orderRepository;

    @Override
    @Tool(description = "Calculates the Gross Merchandise Value (GMV) from a list of orders based on the provided filters.")
    public double getGmv(OrderInsightRequestDto request) {
        return computeGmv(loadOrders(request));
    }

    @Override
    @Tool(description = "Counts the number of orders based on the provided filters.")
    public int getOrderCount(OrderInsightRequestDto request) {
        return loadOrders(request).size();
    }

    @Override
    @Tool(description = "Calculates the Average Order Value from a list of orders based on the provided filters.")
    public double getAvgOrderValue(OrderInsightRequestDto request) {
        List<OrderCityView> orders = loadOrders(request);
        double gmv = computeGmv(orders);
        int count = orders.size();
        return count == 0 ? 0 : gmv / count;
    }

    @Override
    @Tool(description = "Calculates the Repeat Rate from a list of orders based on the provided filters.")
    public double getRepeatRate(OrderInsightRequestDto request) {
        return calculateRepeatRate(loadOrders(request));
    }

    @Override
    @Tool(description = "Calculates the Average Fulfillment Hours from a list of orders based on the provided filters.")
    public double getAvgFulfillmentHours(OrderInsightRequestDto request) {
        return calculateAvgFulfillmentTime(loadOrders(request));
    }

    @Override
    @Tool(description = "Calculates the City Distribution from a list of orders based on the provided filters.")
    public Map<String, Integer> getCityDistribution(OrderInsightRequestDto request) {
        return calculateCityDistribution(loadOrders(request));
    }

    @Override
    @Tool(description = "Calculates the Category Distribution from a list of orders based on the provided filters.")
    public Map<String, Integer> getCategoryDistribution(OrderInsightRequestDto request) {
        return calculateCategoryDistribution(loadOrders(request));
    }

    private List<OrderCityView> loadOrders(OrderInsightRequestDto request) {
        LocalDateTime start = request.startDate();
        LocalDateTime end = request.endDate();
        String city = request.city();
        String category = request.category();
        return orderRepository.findOrdersByFilters(start, end, city, category);
    }

    private double computeGmv(List<OrderCityView> orders) {
        return orders.stream()
                .mapToDouble(o -> o.getPrice() == null ? 0.0 : o.getPrice())
                .sum();
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
