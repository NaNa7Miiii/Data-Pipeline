package com.example.demo.tool;

import com.example.demo.dto.request.OrderInsightRequestDto;
import com.example.demo.service.interfaces.SellerInsightService;
import org.springframework.ai.tool.annotation.Tool;

import java.util.Map;

public class SellerQueryTools {
    private final SellerInsightService service;

    public SellerQueryTools(SellerInsightService service) {
        this.service = service;
    }

    @Tool(description = "Calculates the HHI (Herfindahl–Hirschman Index) score based on the market share of each seller from a list of orders based on the provided filters, " +
            "where HHI < 0.01 indicates a highly competitive industry, 0.01 <= HHI < 0.15 indicates an unconcentrated industry, 0.15 <= HHI <= 0.25 indicates moderate concentration, and HHI > 0.25 indicates high concentration.")
    public double getHHI(OrderInsightRequestDto request) { return service.getHHI(request); }

    @Tool(description = "Calculates the market share of each seller from a list of orders based on the provided filters")
    public Map<String, Double> getMarketShare(OrderInsightRequestDto request) {return service.getMarketShare(request); };

    @Tool(description = "Calculates the concentration ratio (CR) based on the market share of each seller from a list of orders based on the provided filters, " +
            "where CR <= 0.4 indicates low concentration, 0.4 < CR <= 0.7 indicates medium concentration, and 0.7 < CR <= 1 indicates high concentration.")
    public double getConcentrationRatio(OrderInsightRequestDto request) { return service.getConcentrationRatio(request); }

    @Tool(description = "Calculates the city distribution of sellers from a list of orders based on the provided filters.")
    public Map<String, Integer> getSellerCityDistribution(OrderInsightRequestDto request) { return service.getSellerCityDistribution(request); }
}
