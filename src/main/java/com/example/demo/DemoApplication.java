package com.example.demo;

import com.example.demo.service.interfaces.OrderInsightService;
import com.example.demo.tool.MathTool;
import com.example.demo.tool.QueryTools;
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
    public ToolCallbackProvider queryTools(OrderInsightService orderInsightService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(new QueryTools(orderInsightService))
                .build();
    }
}
