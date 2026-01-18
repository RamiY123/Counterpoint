package com.example.demo.config;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ApiKeyHolder {
    
    private final AtomicReference<String> apiKey = new AtomicReference<>();
    
    public void setApiKey(String key) {
        this.apiKey.set(key);
    }
    
    public String getApiKey() {
        return this.apiKey.get();
    }
    
    public boolean hasApiKey() {
        String key = apiKey.get();
        return key != null && !key.isEmpty() && !key.startsWith("sk-placeholder");
    }
}
