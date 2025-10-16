package com.finalproject.dataplatform.dto.requests;

public record CustomerRequestDto(
        String customerId,
        String customerUniqueId,
        Integer customerZipCodePrefix,
        String customerCity,
        String customerState
) {}