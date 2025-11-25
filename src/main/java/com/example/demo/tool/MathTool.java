package com.example.demo.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

public class MathTool {
    @Tool(description = "Adds two numbers together")
    public Integer addNumbers(Integer a, Integer b) {
        return a + b;
    }

    @Tool(description = "Subtracts the second number from the first")
    public Integer subtractNumbers(Integer a, Integer b) {
        return a - b;
    }

    @Tool(description = "Multiplies two numbers together")
    public Integer multiplyNumbers(Integer a, Integer b) {
        return a * b;
    }

    @Tool(description = "Divides the first number by the second")
    public Integer divideNumbers(Integer a, Integer b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        return a / b;
    }
}
