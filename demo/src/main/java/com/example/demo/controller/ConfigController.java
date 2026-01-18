package com.example.demo.controller;

import com.example.demo.config.ApiKeyHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
@Slf4j
public class ConfigController {
    
    private final ApiKeyHolder apiKeyHolder;
    
    @Value("${spring.ai.openai.api-key:}")
    private String configuredApiKey;
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        
        boolean hasKey = apiKeyHolder.hasApiKey() || isValidKey(configuredApiKey);
        status.put("apiKeyConfigured", hasKey);
        status.put("message", hasKey ? "API key is configured" : "API key not configured");
        
        return ResponseEntity.ok(status);
    }
    
    @PostMapping("/api-key")
    public ResponseEntity<Map<String, Object>> setApiKey(@RequestBody Map<String, String> request) {
        String apiKey = request.get("apiKey");
        
        Map<String, Object> response = new HashMap<>();
        
        if (apiKey == null || apiKey.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "API key cannot be empty");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (!apiKey.startsWith("sk-")) {
            response.put("success", false);
            response.put("message", "Invalid API key format. Key should start with 'sk-'");
            return ResponseEntity.badRequest().body(response);
        }
        
        apiKeyHolder.setApiKey(apiKey);
        
        // Also set as system property so OpenAI client picks it up
        System.setProperty("spring.ai.openai.api-key", apiKey);
        
        log.info("API key updated via UI");
        
        response.put("success", true);
        response.put("message", "API key configured successfully");
        return ResponseEntity.ok(response);
    }
    
    private boolean isValidKey(String key) {
        return key != null && !key.isEmpty() && 
               !key.equals("sk-placeholder-will-be-set-at-runtime") &&
               key.startsWith("sk-");
    }
}
