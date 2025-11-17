package com.finalproject.dataplatform.dto.responses;

import java.util.Map;

public record OrderInsightResponseDto(
        Double gmv,
        Integer OrderCount,
        Double avgOrderValue,
        Double repeatRate,
        Double avgFulfillmentTime,
        Map<String, Integer> cityOrderDistribution,
        Map<String, Integer> categoryOrderDistribution
) {}
