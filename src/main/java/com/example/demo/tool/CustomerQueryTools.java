package com.example.demo.tool;

import com.example.demo.dto.request.OrderInsightRequestDto;
import com.example.demo.service.interfaces.CustomerInsightService;
import org.springframework.ai.tool.annotation.Tool;

import java.util.Map;

public class CustomerQueryTools {
    private final CustomerInsightService service;

    public CustomerQueryTools(CustomerInsightService service) {
        this.service = service;
    }

    @Tool(description = "Counts the number of unique customers from a list of orders based on the provided filters.")
    public int getCustomerCount(OrderInsightRequestDto request) { return service.getCustomerCount(request); }

    @Tool(description = "Calculates the average customer life value from a list of orders based on the provided filters.")
    public double getAvgCLV(OrderInsightRequestDto request) {
        return service.getAvgCLV(request);
    }

    @Tool(description = "Calculates the city distribution of customers from a list of orders based on the provided filters.")
    public Map<String, Integer> getCustomerCityDistribution(OrderInsightRequestDto request) { return service.getCustomerCityDistribution(request); }

    @Tool(description = "Calculates the repeat rate of customers from a list of orders based on the provided filters.")
    public double getRepeatRate(OrderInsightRequestDto request) {
        return service.getRepeatRate(request);
    }

}
