package com.example.demo.dto;

import com.example.demo.model.Debate;
import com.example.demo.model.Agent;
import com.example.demo.model.Message;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
    private Long debateId;
    private String topic;
    private String language;
    private Debate.DebateStatus status;
    private int totalRounds;
    private int currentRound;
    private String conclusion;
    private List<AgentDto> agents;
    private List<MessageDto> messages;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentDto {
        private Long id;
        private String name;
        private String stance;
        private Agent.AgentRole role;
    }
    
    public static HistoryResponse from(Debate debate, List<Message> messages) {
        HistoryResponse response = new HistoryResponse();
        response.setDebateId(debate.getId());
        response.setTopic(debate.getTopic());
        response.setLanguage(debate.getLanguage());
        response.setStatus(debate.getStatus());
        response.setTotalRounds(debate.getTotalRounds());
        response.setCurrentRound(debate.getCurrentRound());
        response.setConclusion(debate.getConclusion());
        
        response.setAgents(debate.getAgents().stream()
            .map(a -> new AgentDto(a.getId(), a.getName(), a.getStance(), a.getRole()))
            .collect(Collectors.toList()));
        
        response.setMessages(messages.stream()
            .map(m -> {
                MessageDto dto = new MessageDto();
                dto.setId(m.getId());
                dto.setContent(m.getContent());
                dto.setAgentName(m.getAgent().getName());
                dto.setAgentRole(m.getAgent().getRole());
                dto.setRoundNumber(m.getRound().getRoundNumber());
                dto.setSequenceNumber(m.getSequenceNumber());
                dto.setTimestamp(m.getTimestamp());
                return dto;
            })
            .collect(Collectors.toList()));
        
        return response;
    }
}
