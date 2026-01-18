package com.example.demo.dto;

import com.example.demo.model.Agent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String content;
    private String agentName;
    private Agent.AgentRole agentRole;
    private int roundNumber;
    private int sequenceNumber;
    private LocalDateTime timestamp;
    private String type = "message";
}
