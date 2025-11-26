package com.example.demo.dto.request;


public record OrderInsightRequestDto(
        String startDate,
        String endDate,
        String city,
        String category
) {}

