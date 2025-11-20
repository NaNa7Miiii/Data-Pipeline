package com.example.demo.tool;

import com.example.demo.dto.request.OrderInsightRequestDto;
import com.example.demo.service.interfaces.OrderInsightService;
import org.springframework.ai.tool.annotation.Tool;

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
}
