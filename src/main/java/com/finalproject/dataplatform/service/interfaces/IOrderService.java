package com.finalproject.dataplatform.service.interfaces;

import com.finalproject.dataplatform.dto.requests.OrderRequestDto;
import com.finalproject.dataplatform.dto.responses.OrderResponseDto;

import java.util.List;

public interface IOrderService {

    List<OrderResponseDto> getAll();

    OrderResponseDto getById(String id);

    OrderResponseDto create(OrderRequestDto orderRequestDto);

    OrderResponseDto updateById(String id, OrderRequestDto orderRequestDto);

    void deleteById(String id);

    // 为 recall-粗排-精排预留接口
    List<OrderResponseDto> recallOrders(String recallType);

    List<OrderResponseDto> rankOrders(List<OrderResponseDto> orders);

    List<OrderResponseDto> rerankOrders(List<OrderResponseDto> orders);
}

