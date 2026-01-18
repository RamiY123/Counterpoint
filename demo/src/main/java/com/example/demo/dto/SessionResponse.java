package com.example.demo.dto;

import com.example.demo.model.Debate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {
    private Long debateId;
    private String topic;
    private String language;
    private int totalRounds;
    private Debate.DebateStatus status;
    
    public static SessionResponse from(Debate debate) {
        return new SessionResponse(
            debate.getId(),
            debate.getTopic(),
            debate.getLanguage(),
            debate.getTotalRounds(),
            debate.getStatus()
        );
    }
}
