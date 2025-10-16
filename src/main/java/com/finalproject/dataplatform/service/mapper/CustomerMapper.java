package com.finalproject.dataplatform.service.mapper;

import com.finalproject.dataplatform.dto.requests.CustomerRequestDto;
import com.finalproject.dataplatform.dto.responses.CustomerResponseDto;
import com.finalproject.dataplatform.model.Customer;

public class CustomerMapper {

    public static CustomerResponseDto toDTO(Customer entity) {
        if (entity == null) return null;
        return new CustomerResponseDto(
                entity.getCustomerId(),
                entity.getCustomerUniqueId(),
                entity.getCustomerZipCodePrefix(),
                entity.getCustomerCity(),
                entity.getCustomerState()
        );
    }

    public static Customer toEntity(CustomerRequestDto dto) {
        if (dto == null) return null;
        Customer entity = new Customer();
        entity.setCustomerId(dto.customerId());
        entity.setCustomerUniqueId(dto.customerUniqueId());
        entity.setCustomerZipCodePrefix(dto.customerZipCodePrefix());
        entity.setCustomerCity(dto.customerCity());
        entity.setCustomerState(dto.customerState());
        return entity;
    }
}
