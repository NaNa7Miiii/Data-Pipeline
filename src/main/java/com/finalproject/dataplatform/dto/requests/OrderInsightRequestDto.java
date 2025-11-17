package com.finalproject.dataplatform.dto.requests;

import java.time.LocalDateTime;

public record OrderInsightRequestDto(
        LocalDateTime startDate,
        LocalDateTime endDate,
        String city,
        String category
) {}
