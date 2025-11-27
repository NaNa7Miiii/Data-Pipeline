package com.example.demo.service.implementation;

import com.example.demo.dto.request.OrderInsightRequestDto;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.interfaces.OrderCityView;
import com.example.demo.service.interfaces.OrderInsightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderInsightServiceImpl implements OrderInsightService {
    @Autowired
    public OrderRepository orderRepository;

    @Override
    // returns the Gross Merchandise Value (GMV)
    public double getGMV(OrderInsightRequestDto request) {
        return computeGMV(loadOrders(request));
    }

    @Override
    // returns the number of orders
    public int getOrderCount(OrderInsightRequestDto request) {
        return loadOrders(request).size();
    }

    @Override
    // returns the average order value
    public double getAvgOrderValue(OrderInsightRequestDto request) {
        List<OrderCityView> orders = loadOrders(request);
        double gmv = computeGMV(orders);
        int count = orders.size();
        return count == 0 ? 0 : gmv / count;
    }

    @Override
    // returns the average fulfillment hours, from purchase to delivered
    public double getAvgFulfillmentHours(OrderInsightRequestDto request) {
        return calculateAvgDuration(loadOrders(request), OrderCityView::getOrderPurchaseTimestamp, OrderCityView::getOrderDeliveredCustomerDate);
    }

    @Override
    // returns the average time in hours required for carrier pickup since order approved
    public double getAvgCarrierPickupHours(OrderInsightRequestDto request) {
        return calculateAvgDuration(loadOrders(request), OrderCityView::getOrderApprovedAt, OrderCityView::getOrderDeliveredCarrierDate);
    }

    @Override
    // returns the delivery SLA compliance rate: rate of orders that delivered on time (as per estimated delivery time)
    public double getDeliverySLAComplianceRate(OrderInsightRequestDto request) { return deliverySLAComplianceRate(loadOrders(request)); }

    @Override
    // returns the cancelling rate of orders
    public double getCancelRate(OrderInsightRequestDto request) { return cancelRate(loadOrders(request)); }

    @Override
    // returns the rate of orders that have a review score less than 3
    public double getLowReviewScoreRate(OrderInsightRequestDto request) { return lowReviewScoreRate(loadOrders(request)); }

    @Override
    // returns the product category with the lowest average review scores
    public String getLowestAvgReviewCategory(OrderInsightRequestDto request) {
        return categoryAvgReviews(loadOrders(request)).entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    // returns the product category with the highest average review scores
    public String getHighestAvgReviewCategory(OrderInsightRequestDto request) {
        return categoryAvgReviews(loadOrders(request)).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    // returns the product category distribution or orders
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

    private double computeGMV(List<OrderCityView> orders) {
        return orders.stream()
                .mapToDouble(o -> o.getPrice() == null ? 0.0 : o.getPrice())
                .sum();
    }



    // function to calculate average fulfillment time (should be implemented via PRD file)
    // average fulfillment time = avg([orderDeliveredCustomerDate - orderPurchaseTimestamp])
//    private double calculateAvgFulfillmentTime(List<OrderCityView> orders) {
//        OptionalDouble averageHours = orders.stream()
//                .filter(order -> order.getOrderDeliveredCustomerDate() != null && order.getOrderPurchaseTimestamp() != null)
//                .mapToLong(order -> java.time.Duration.between(
//                        order.getOrderPurchaseTimestamp(),
//                        order.getOrderDeliveredCustomerDate()).toHours())
//                .average();
//        return averageHours.orElse(0.0);
//    }

    private double calculateAvgDuration(List<OrderCityView> orders, Function<OrderCityView, LocalDateTime> start, Function<OrderCityView, LocalDateTime> end) {
        OptionalDouble averageHours = orders.stream()
                .filter(order -> order.getOrderDeliveredCustomerDate() != null && order.getOrderPurchaseTimestamp() != null)
                .mapToLong(order -> java.time.Duration.between(
                        start.apply(order),
                        end.apply(order)).toHours())
                .average();
        return averageHours.orElse(0.0);
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

    private double cancelRate(List<OrderCityView> orders) {
        long cancelled = orders.stream()
                .filter(order -> order.getReviewScore() != null && order.getOrderStatus().equals("canceled"))
                .count();
        int count = orders.size();
        return count == 0 ? 0 : (double) cancelled / count;
    }

    private double lowReviewScoreRate(List<OrderCityView> orders) {
        long lowScores = orders.stream()
                .filter(order -> order.getReviewScore() != null && order.getReviewScore() < 3)
                .count();
        long count = orders.stream()
                .filter(order -> order.getReviewScore() != null)
                .count();
        return count == 0 ? 0 : (double) lowScores / count;
    }

    private Map<String, Double> categoryAvgReviews(List<OrderCityView> orders) {
        return orders.stream()
                .filter(order -> order.getReviewScore() != null)
                .collect(Collectors.groupingBy(
                        OrderCityView::getProductCategoryNameEnglish,
                        Collectors.averagingDouble(OrderCityView::getReviewScore)
                ));
    }

//    count(delivered date <= estimated delivery date) / count(delivered)
    private double deliverySLAComplianceRate(List<OrderCityView> orders) {
        long delivered = orders.stream()
                .filter(order -> order.getOrderStatus() != null && order.getOrderStatus().equals("delivered"))
                .count();
        long onTime = orders.stream()
                .filter(order -> order.getOrderStatus() != null && order.getOrderStatus().equals("delivered"))
                .filter(order -> order.getOrderDeliveredCustomerDate().isBefore(order.getOrderEstimatedDeliveryDate()))
                .count();
        return delivered == 0 ? 0 : (double) onTime / delivered;
    }
}
