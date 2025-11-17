package com.finalproject.dataplatform.controller;

import com.finalproject.dataplatform.dto.requests.OrderInsightRequestDto;
import com.finalproject.dataplatform.dto.responses.OrderInsightResponseDto;
import com.finalproject.dataplatform.service.interfaces.OrderInsightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/insights")
public class OrderInsightController {
    @Autowired
    private OrderInsightService orderInsightService;

    @PostMapping("/orders")
    public ResponseEntity<OrderInsightResponseDto> getOrderInsights(@RequestBody OrderInsightRequestDto request) {
        OrderInsightResponseDto response = orderInsightService.getOrderInsights(request);
        return ResponseEntity.ok(response);
    }
}
