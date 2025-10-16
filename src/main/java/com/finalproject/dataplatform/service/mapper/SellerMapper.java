package com.finalproject.dataplatform.service.mapper;

import com.finalproject.dataplatform.dto.requests.SellerRequestDto;
import com.finalproject.dataplatform.dto.responses.SellerResponseDto;
import com.finalproject.dataplatform.model.Seller;

public class SellerMapper {

    public static SellerResponseDto toDTO(Seller entity) {
        if (entity == null) return null;
        return new SellerResponseDto (
                entity.getSellerId(),
                entity.getSellerZipCodePrefix(),
                entity.getSellerCity(),
                entity.getSellerState()
        );
    }

    public static Seller toEntity(SellerRequestDto dto) {
        if (dto == null) return null;
        Seller entity = new Seller();
        entity.setSellerId(dto.sellerId());
        entity.setSellerZipCodePrefix(dto.sellerZipCodePrefix());
        entity.setSellerCity(dto.sellerCity());
        entity.setSellerState(dto.sellerState());
        return entity;
    }
}