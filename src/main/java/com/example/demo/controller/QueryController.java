package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class QueryController {
    private static final Logger log = LoggerFactory.getLogger(QueryController.class);
    private final ChatClient chatClient;

    public QueryController(ChatClient.Builder builder, ToolCallbackProvider tools) {
        Arrays.stream(tools.getToolCallbacks()).forEach(t -> {
            log.info("Registered tool: {}", t.getToolDefinition());
        });
        this.chatClient = builder
                .defaultToolCallbacks(tools)
                .build();
    }

    @GetMapping("/chat")
    public String chat() {
        return chatClient.prompt()
                .user(String.valueOf(new UserMessage("Calculate GMV for startDate: 2017-12-01T00:00:00, endDate: 2017-12-31T23:59:59, city: sao paulo, category: housewares")))
                .call()
                .content();
    }
}