package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.dto.MessageDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebateOrchestrator {
    
    private final AgentService agentService;
    private final DebateService debateService;
    private final SimpMessagingTemplate messagingTemplate;
    
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
    
    @Async("debateExecutor")
    public void startDebate(Long debateId) {
        log.info("Starting debate orchestration for debate ID: {}", debateId);
        
        try {
            Debate debate = debateService.getDebate(debateId);
            debateService.updateDebateStatus(debateId, Debate.DebateStatus.IN_PROGRESS);
            
            sendStatusUpdate(debateId, "started", "Debate is starting...");
            
            Agent proponent = debate.getAgents().stream()
                .filter(a -> a.getRole() == Agent.AgentRole.PROPONENT)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Proponent not found"));
            
            Agent opponent = debate.getAgents().stream()
                .filter(a -> a.getRole() == Agent.AgentRole.OPPONENT)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Opponent not found"));
            
            List<Message> conversationHistory = new ArrayList<>();
            
            for (int round = 1; round <= debate.getTotalRounds(); round++) {
                log.info("Starting round {} of debate {}", round, debateId);
                debateService.updateCurrentRound(debateId, round);
                sendStatusUpdate(debateId, "round_start", "Round " + round + " starting");
                
                DebateRound debateRound = debateService.createRound(debate, round);
                
                // Proponent speaks
                String proponentResponse = generateWithRetry(proponent, debate.getTopic(), 
                    debate.getLanguage(), conversationHistory, debateId);
                Message proponentMessage = debateService.saveMessage(proponent, debateRound, 
                    proponentResponse, 1);
                conversationHistory.add(proponentMessage);
                sendMessage(debateId, proponentMessage, round);
                
                Thread.sleep(1000); // Brief pause between speakers
                
                // Opponent responds
                String opponentResponse = generateWithRetry(opponent, debate.getTopic(), 
                    debate.getLanguage(), conversationHistory, debateId);
                Message opponentMessage = debateService.saveMessage(opponent, debateRound, 
                    opponentResponse, 2);
                conversationHistory.add(opponentMessage);
                sendMessage(debateId, opponentMessage, round);
                
                sendStatusUpdate(debateId, "round_end", "Round " + round + " completed");
                
                if (round < debate.getTotalRounds()) {
                    Thread.sleep(2000); // Pause between rounds
                }
            }
            
            // Generate conclusion
            sendStatusUpdate(debateId, "generating_conclusion", "Generating debate conclusion...");
            String conclusion = agentService.generateConclusion(
                debate.getTopic(), 
                debate.getLanguage(), 
                conversationHistory, 
                debate.getAgents()
            );
            debateService.saveConclusion(debateId, conclusion);
            sendConclusion(debateId, conclusion);
            
            debateService.updateDebateStatus(debateId, Debate.DebateStatus.COMPLETED);
            sendStatusUpdate(debateId, "completed", "Debate completed successfully");
            
            log.info("Debate {} completed successfully", debateId);
            
        } catch (Exception e) {
            log.error("Error during debate {}: {}", debateId, e.getMessage(), e);
            debateService.updateDebateStatus(debateId, Debate.DebateStatus.CANCELLED);
            handleError(debateId, e);
        }
    }
    
    private String generateWithRetry(Agent agent, String topic, String language, 
            List<Message> history, Long debateId) {
        int maxRetries = 3;
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return agentService.generateResponse(agent, topic, language, history);
            } catch (Exception e) {
                lastException = e;
                log.warn("Attempt {} failed for agent {}: {}", attempt, agent.getName(), e.getMessage());
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(2000 * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted during retry", ie);
                    }
                }
            }
        }
        throw new RuntimeException("Failed to generate response after " + maxRetries + " attempts", lastException);
    }
    
    private void sendMessage(Long debateId, Message message, int round) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setAgentName(message.getAgent().getName());
        dto.setAgentRole(message.getAgent().getRole());
        dto.setRoundNumber(round);
        dto.setSequenceNumber(message.getSequenceNumber());
        dto.setTimestamp(message.getTimestamp());
        dto.setType("message");
        
        messagingTemplate.convertAndSend("/topic/debate/" + debateId, dto);
    }
    
    private void sendStatusUpdate(Long debateId, String status, String message) {
        Map<String, Object> update = new HashMap<>();
        update.put("type", "status");
        update.put("status", status);
        update.put("message", message);
        update.put("timestamp", LocalDateTime.now().toString());
        
        messagingTemplate.convertAndSend("/topic/debate/" + debateId, update);
    }
    
    private void sendConclusion(Long debateId, String conclusion) {
        Map<String, Object> update = new HashMap<>();
        update.put("type", "conclusion");
        update.put("conclusion", conclusion);
        update.put("timestamp", LocalDateTime.now().toString());
        
        messagingTemplate.convertAndSend("/topic/debate/" + debateId, update);
    }
    
    private void handleError(Long debateId, Exception e) {
        String errorMessage = e.getMessage();
        
        // Check for API key related errors
        if (errorMessage != null && (
                errorMessage.contains("401") || 
                errorMessage.contains("Unauthorized") ||
                errorMessage.contains("Invalid API") ||
                errorMessage.contains("API key") ||
                errorMessage.contains("placeholder"))) {
            errorMessage = API_KEY_HELP;
        }
        
        Map<String, Object> error = new HashMap<>();
        error.put("type", "error");
        error.put("message", errorMessage);
        error.put("timestamp", LocalDateTime.now().toString());
        
        messagingTemplate.convertAndSend("/topic/debate/" + debateId, error);
    }
}
