package com.finalproject.dataplatform.dto.responses;

public record CustomerResponseDto(
        String customerId,
        String customerUniqueId,
        Integer customerZipCodePrefix,
        String customerCity,
        String customerState
) {}
