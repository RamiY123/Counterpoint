package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigRequest {
    private String topic;
    private String language = "en";
    private int rounds = 3;
    private AgentConfig agentA;
    private AgentConfig agentB;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentConfig {
        private String name;
        private String stance;
        private String personality;
    }
}
