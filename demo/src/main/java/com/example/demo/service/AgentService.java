package com.example.demo.service;

import com.example.demo.model.Agent;
import com.example.demo.model.Message;
import com.example.demo.config.OpenAiConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentService {
    
    private final ObjectProvider<ChatClient> chatClientProvider;
    private final OpenAiConfig openAiConfig;
    
    public String generateResponse(Agent agent, String topic, String language, List<Message> conversationHistory) {
        String systemPrompt = buildSystemPrompt(agent, topic, language);
        String conversationContext = buildConversationContext(conversationHistory);
        
        String userPrompt = conversationContext.isEmpty() 
            ? "Please provide your opening argument on the topic: " + topic
            : "Previous arguments:\n" + conversationContext + "\n\nPlease respond to the latest argument.";
        
        log.info("Agent {} generating response for topic: {}", agent.getName(), topic);
        
        ChatClient chatClient = chatClientProvider.getObject();
        
        String response = chatClient.prompt()
            .system(systemPrompt)
            .user(userPrompt)
            .call()
            .content();
        
        log.info("Agent {} response generated successfully", agent.getName());
        return response;
    }
    
    public String generateConclusion(String topic, String language, List<Message> allMessages, List<Agent> agents) {
        String agentAName = agents.stream()
            .filter(a -> a.getRole() == Agent.AgentRole.PROPONENT)
            .map(Agent::getName)
            .findFirst()
            .orElse("Agent A");
        
        String agentBName = agents.stream()
            .filter(a -> a.getRole() == Agent.AgentRole.OPPONENT)
            .map(Agent::getName)
            .findFirst()
            .orElse("Agent B");
        
        String langInstruction = "en".equals(language) 
            ? "Respond in English." 
            : "Respond in Arabic (العربية).";
        
        String systemPrompt = String.format("""
            You are an impartial debate moderator. Your task is to provide a balanced conclusion 
            summarizing a debate between %s and %s on the topic: "%s".
            
            %s
            
            Provide a concise conclusion (max 150 words) that:
            1. Summarizes the key arguments from both sides
            2. Highlights areas of agreement and disagreement
            3. Does NOT declare a winner
            4. Leaves the final judgment to the reader
            """, agentAName, agentBName, topic, langInstruction);
        
        String debateTranscript = allMessages.stream()
            .map(m -> m.getAgent().getName() + ": " + m.getContent())
            .collect(Collectors.joining("\n\n"));
        
        String userPrompt = "Here is the full debate transcript:\n\n" + debateTranscript + 
            "\n\nPlease provide your conclusion.";
        
        log.info("Generating debate conclusion for topic: {}", topic);
        
        ChatClient chatClient = chatClientProvider.getObject();
        
        return chatClient.prompt()
            .system(systemPrompt)
            .user(userPrompt)
            .call()
            .content();
    }
    
    private String buildSystemPrompt(Agent agent, String topic, String language) {
        int maxWords = openAiConfig.getMaxResponseWords();
        String langInstruction = "en".equals(language) 
            ? "Respond in English." 
            : "Respond in Arabic (العربية).";
        
        return String.format("""
            You are %s, a debate participant with the following characteristics:
            - Stance: %s
            - Personality: %s
            
            You are participating in a structured debate about: %s
            
            Rules:
            1. Stay firmly committed to your stance
            2. Be persuasive but respectful
            3. Address counterarguments directly
            4. Use evidence and logical reasoning
            5. Keep responses concise (maximum %d words)
            6. %s
            
            Never break character or agree with the opposing view.
            """, 
            agent.getName(),
            agent.getStance(),
            agent.getPersonality() != null ? agent.getPersonality() : "Professional and articulate",
            topic,
            maxWords,
            langInstruction
        );
    }
    
    private String buildConversationContext(List<Message> history) {
        if (history == null || history.isEmpty()) {
            return "";
        }
        
        return history.stream()
            .map(m -> m.getAgent().getName() + ": " + m.getContent())
            .collect(Collectors.joining("\n\n"));
    }
}
