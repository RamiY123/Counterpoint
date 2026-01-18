package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.HashMap;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    private static final String API_KEY_HELP = """
        
        ❌ Invalid OpenAI API Key
        
        Your API key is missing or invalid. Please configure it:
        
        🔧 Option 1: Environment Variable
           PowerShell: $env:OPENAI_API_KEY="sk-your-key"
           Then restart the server
        
        🔧 Option 2: Edit application-dev.yml
           spring.ai.openai.api-key: sk-your-key
        
        🔗 Get your key: https://platform.openai.com/api-keys
        """;
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("Unhandled exception: {}", e.getMessage(), e);
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", true);
        
        String message = e.getMessage();
        if (message != null && (
                message.contains("401") || 
                message.contains("Unauthorized") ||
                message.contains("Invalid API") ||
                message.contains("API key") ||
                message.contains("placeholder"))) {
            error.put("message", API_KEY_HELP);
        } else {
            error.put("message", message != null ? message : "An unexpected error occurred");
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
