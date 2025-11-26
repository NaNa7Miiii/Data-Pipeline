package com.example.demo.service.implementation;

import com.example.demo.dto.request.OrderInsightRequestDto;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.interfaces.OrderCityView;
import com.example.demo.service.interfaces.OrderInsightService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    // --- 关键修改：手动解析 String 到 LocalDateTime ---
    private List<OrderCityView> loadOrders(OrderInsightRequestDto request) {
        // 使用 parseSmartDate 方法处理 String
        LocalDateTime start = parseSmartDate(request.startDate(), true);
        LocalDateTime end = parseSmartDate(request.endDate(), false);

        String city = request.city();
        String category = request.category();
        return orderRepository.findOrdersByFilters(start, end, city, category);
    }

    private LocalDateTime parseSmartDate(String dateStr, boolean isStart) {
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            if (dateStr.contains("T")) {
                return LocalDateTime.parse(dateStr);
            }
            LocalDate date = LocalDate.parse(dateStr);
            return isStart ? date.atStartOfDay() : date.atTime(LocalTime.MAX);
        } catch (Exception e) {
            System.err.println("Date parsing failed for: " + dateStr);
            return null;
        }
    }

    private double computeGmv(List<OrderCityView> orders) {
        return orders.stream()
                .mapToDouble(o -> o.getPrice() == null ? 0.0 : o.getPrice())
                .sum();
    }

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

    private double calculateAvgFulfillmentTime(List<OrderCityView> orders) {
        OptionalDouble averageHours = orders.stream()
                .filter(order -> order.getOrderDeliveredCustomerDate() != null && order.getOrderPurchaseTimestamp() != null)
                .mapToLong(order -> java.time.Duration.between(
                        order.getOrderPurchaseTimestamp(),
                        order.getOrderDeliveredCustomerDate()).toHours())
                .average();
        return averageHours.orElse(0.0);
    }

    private Map<String, Integer> calculateCityDistribution(List<OrderCityView> orders) {
        Map<String, Integer> distribution = new HashMap<>();
        for (OrderCityView order : orders) {
            String city = order.getCustomerCity();
            if (city == null) continue;
            distribution.put(city, distribution.getOrDefault(city, 0) + 1);
        }
        return distribution;
    }

    private Map<String, Integer> calculateCategoryDistribution(List<OrderCityView> orders) {
        Map<String, Integer> distribution = new HashMap<>();
        for (OrderCityView order : orders) {
            String category = order.getProductCategoryNameEnglish();
            if (category == null) continue;
            distribution.put(category, distribution.getOrDefault(category, 0) + 1);
        }
        return distribution;
    }

    @Override
    public String getOrderStatus(String orderId) {
        String status = orderRepository.findStatusById(orderId);
        return status != null ? status : "Order not found";
    }

    @Override
    public Map<String, Double> getTopSellers(OrderInsightRequestDto request) {
        LocalDateTime start = parseSmartDate(request.startDate(), true);
        LocalDateTime end = parseSmartDate(request.endDate(), false);

        List<Map<String, Object>> results = orderRepository.findTopSellers(
                start,
                end,
                5
        );

        Map<String, Double> topSellers = new HashMap<>();
        for (Map<String, Object> row : results) {
            String sellerId = (String) row.get("seller_id");
            Double sales = row.get("total_sales") != null ? ((Number) row.get("total_sales")).doubleValue() : 0.0;
            topSellers.put(sellerId, sales);
        }
        return topSellers;
    }
    @Override
    @Tool(description = "Identifies the top selling products based on total sales volume within the specified date range.")
    public Map<String, Integer> getTopProducts(OrderInsightRequestDto request) {
        LocalDateTime start = parseSmartDate(request.startDate(), true);
        LocalDateTime end = parseSmartDate(request.endDate(), false);

        List<Map<String, Object>> results = orderRepository.findTopProducts(
                start, end, request.city(), request.category(), 5);
        Map<String, Integer> topProducts = new HashMap<>();
        for (Map<String, Object> row : results) {
            String productId = (String) row.get("product_id");
            Integer volume = row.get("sales_volume") != null ? ((Number) row.get("sales_volume")).intValue() : 0;
            topProducts.put(productId, volume);
        }
        return topProducts;
    }

    @Override
    @Tool(description = "Analyzes the monthly sales trend (GMV) within the specified date range. Returns a map of 'YYYY-MM' to total sales.")
    public Map<String, Double> getSalesTrend(OrderInsightRequestDto request) {
        LocalDateTime start = parseSmartDate(request.startDate(), true);
        LocalDateTime end = parseSmartDate(request.endDate(), false);

        List<Map<String, Object>> results = orderRepository.findMonthlySalesTrend(
                start, end, request.city(), request.category());
        Map<String, Double> trend = new java.util.LinkedHashMap<>(); // 使用 LinkedHashMap 保持月份顺序
        for (Map<String, Object> row : results) {
            String month = (String) row.get("month");
            Double sales = row.get("total_sales") != null ? ((Number) row.get("total_sales")).doubleValue() : 0.0;
            trend.put(month, sales);
        }
        return trend;
    }
}