package com.finalproject.dataplatform.service.implementation;

import com.finalproject.dataplatform.dto.requests.OrderRequestDto;
import com.finalproject.dataplatform.dto.responses.OrderResponseDto;
import com.finalproject.dataplatform.model.Order;
import com.finalproject.dataplatform.repository.OrderRepository;
import com.finalproject.dataplatform.service.interfaces.IOrderService;
import com.finalproject.dataplatform.service.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderResponseDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponseDto) // 静态方法
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto getById(String id) {
        return orderRepository.findById(Integer.valueOf(id))
                .map(OrderMapper::toResponseDto)
                .orElse(null);
    }

    @Override
    public OrderResponseDto create(OrderRequestDto orderRequestDto) {
        Order entity = OrderMapper.toEntity(orderRequestDto); // 注意实体类名
        Order saved = orderRepository.save(entity);
        return OrderMapper.toResponseDto(saved);
    }

    @Override
    public OrderResponseDto updateById(String id, OrderRequestDto orderRequestDto) {
        Order actualOrder = orderRepository.findById(Integer.valueOf(id)).orElse(null);
        if (actualOrder == null) {
            return null;
        }
        // 更新字段（建议写一个updateEntityFromDto方法）
        actualOrder.setOrderStatus(orderRequestDto.orderStatus());
        // ...其他字段
        Order updated = orderRepository.save(actualOrder);
        return OrderMapper.toResponseDto(updated);
    }

    @Override
    public void deleteById(String id) {
        orderRepository.deleteById(Integer.valueOf(id));
    }

    @Override
    public List<OrderResponseDto> recallOrders(String recallType) {
        // TODO: 根据 recallType 实现不同召回逻辑
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> rankOrders(List<OrderResponseDto> orders) {
        // TODO: 实现粗排逻辑
        return orders;
    }

    @Override
    public List<OrderResponseDto> rerankOrders(List<OrderResponseDto> orders) {
        // TODO: 实现精排逻辑
        return orders;
    }
}
