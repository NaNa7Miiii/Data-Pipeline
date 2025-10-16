package com.finalproject.dataplatform.service.interfaces;

import com.finalproject.dataplatform.dto.requests.CustomerRequestDto;
import com.finalproject.dataplatform.dto.responses.CustomerResponseDto;

import java.util.List;

public interface ICustomerService {
    List<CustomerResponseDto> getAll();
    CustomerResponseDto getById(String id);
    CustomerResponseDto create(CustomerRequestDto customerRequestDto);
    CustomerResponseDto updateById(String id, CustomerRequestDto customerRequestDto);
    void deleteById(String id);

    // 扩展方法（如有 recall/rank/rerank 等需求可加）
}
