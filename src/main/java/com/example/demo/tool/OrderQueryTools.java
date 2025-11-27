package com.example.demo.tool;

import com.example.demo.dto.request.OrderInsightRequestDto;
import com.example.demo.service.interfaces.OrderInsightService;
import org.springframework.ai.tool.annotation.Tool;

import java.util.Map;

public class OrderQueryTools {
    private final OrderInsightService service;

    public OrderQueryTools(OrderInsightService service) {
        this.service = service;
    }

    @Tool(description = "Calculates the Gross Merchandise Value (GMV) from a list of orders based on the provided filters.")
    public double getGMV(OrderInsightRequestDto request) { return service.getGMV(request); }

    @Tool(description = "Counts the number of orders based on the provided filters.")
    public int getOrderCount(OrderInsightRequestDto request) {
        return service.getOrderCount(request);
    }

    @Tool(description = "Calculates the average order value from a list of orders based on the provided filters.")
    public double getAvgOrderValue(OrderInsightRequestDto request) {
        return service.getAvgOrderValue(request);
    }

    @Tool(description = "Calculates the average fulfillment hours from a list of orders based on the provided filters.")
    public double getAvgFulfillmentHours(OrderInsightRequestDto request) { return service.getAvgFulfillmentHours(request); }

    @Tool(description = "Calculates the average carrier pickup hours from a list of orders based on the provided filters.")
    public double getAvgCarrierPickupHours(OrderInsightRequestDto request) { return service.getAvgCarrierPickupHours(request); }

    @Tool(description = "Calculates the category distribution of orders from a list of orders based on the provided filters.")
    public Map<String, Integer> getCategoryDistribution(OrderInsightRequestDto request) { return service.getCategoryDistribution(request); }

    @Tool(description = "Calculates the rate of orders that meet the delivery SLA compliance, delivering on time as per estimated delivery time, from a list of orders based on the provided filters.")
    public double getDeliverySLAComplianceRate(OrderInsightRequestDto request) { return service.getDeliverySLAComplianceRate(request); }

    @Tool(description = "Calculates the order cancel rate from a list of orders based on the provided filters.")
    public double getCancelRate(OrderInsightRequestDto request) { return service.getCancelRate(request); }

    @Tool(description = "Calculates the low review score rate of orders from a list of orders based on the provided filters. Assume and indicate that a review score less than 3 is considered low.")
    public double getLowReviewScoreRate(OrderInsightRequestDto request) { return service.getLowReviewScoreRate(request); }

    @Tool(description = "Finds the category with the lowest average review score from a list of orders based on the provided filters.")
    public String getLowestAvgReviewCategory(OrderInsightRequestDto request) { return service.getLowestAvgReviewCategory(request); }

    @Tool(description = "Finds the category with the highest average review score from a list of orders based on the provided filters.")
    public String getHighestAvgReviewCategory(OrderInsightRequestDto request) { return service.getHighestAvgReviewCategory(request); }

}
