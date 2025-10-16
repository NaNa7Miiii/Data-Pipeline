package com.finalproject.dataplatform.dto.responses;

public record SellerResponseDto(
        String sellerId,
        Integer sellerZipCodePrefix,
        String sellerCity,
        String sellerState
) {}
