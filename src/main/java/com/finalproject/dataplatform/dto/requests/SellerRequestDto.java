package com.finalproject.dataplatform.dto.requests;

public record SellerRequestDto(
        String sellerId,
        Integer sellerZipCodePrefix,
        String sellerCity,
        String sellerState
) {}