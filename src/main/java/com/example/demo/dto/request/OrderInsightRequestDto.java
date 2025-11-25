package com.example.demo.dto.request;

import java.time.LocalDateTime;

public record OrderInsightRequestDto(
        LocalDateTime startDate,
        LocalDateTime endDate,
        String city,
        String category
) {}

