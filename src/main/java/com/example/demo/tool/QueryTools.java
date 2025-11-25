package com.example.demo.tool;

import com.example.demo.dto.request.OrderInsightRequestDto;
import com.example.demo.service.interfaces.OrderInsightService;
import org.springframework.ai.tool.annotation.Tool;
import java.util.Map;
public class QueryTools {
    private final OrderInsightService service;

    public QueryTools(OrderInsightService service) {
        this.service = service;
    }

    @Tool(description = "Calculates the Gross Merchandise Value (GMV) from a list of orders based on the provided filters.")
    public double getGmv(OrderInsightRequestDto request) {
        return service.getGmv(request);
    }

    @Tool(description = "Counts the number of orders based on the provided filters.")
    public int getOrderCount(OrderInsightRequestDto request) {
        return service.getOrderCount(request);
    }

    @Tool(description = "Calculates the Average Order Value from a list of orders based on the provided filters.")
    public double getAvgOrderValue(OrderInsightRequestDto request) {
        return service.getAvgOrderValue(request);
    }

    @Tool(description = "Calculates the Repeat Rate from a list of orders based on the provided filters.")
    public double getRepeatRate(OrderInsightRequestDto request) {
        return service.getRepeatRate(request);
    }

    @Tool(description = "Query the current status of a specific order by its Order ID.")
    public String getOrderStatus(String orderId) {
        return service.getOrderStatus(orderId);
    }


    @Tool(description = "Identify top performing sellers based on total sales (GMV) within the specified date range.")
    public Map<String, Double> getTopSellers(OrderInsightRequestDto request) {
        return service.getTopSellers(request);
    }
}
