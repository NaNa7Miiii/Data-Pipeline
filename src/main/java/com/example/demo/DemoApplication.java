package com.example.demo;

import com.example.demo.service.interfaces.OrderInsightService;
import com.example.demo.service.interfaces.CustomerInsightService;
import com.example.demo.service.interfaces.SellerInsightService;
import com.example.demo.tool.CustomerQueryTools;
import com.example.demo.tool.OrderQueryTools;
import com.example.demo.tool.SellerQueryTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Bean
    @Primary
    public ToolCallbackProvider queryTools(CustomerInsightService customerInsightService,
                                           OrderInsightService orderInsightService,
                                           SellerInsightService sellerInsightService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(
                        new CustomerQueryTools(customerInsightService),
                        new OrderQueryTools(orderInsightService),
                        new SellerQueryTools(sellerInsightService)
                )
                .build();
    }
}
