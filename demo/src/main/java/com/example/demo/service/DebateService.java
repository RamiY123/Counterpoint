package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.dto.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebateService {
    
    private final DebateRepository debateRepository;
    private final AgentRepository agentRepository;
    private final MessageRepository messageRepository;
    private final DebateRoundRepository roundRepository;
    
    @Transactional
    public Debate createDebate(ConfigRequest config) {
        log.info("Creating debate for topic: {}", config.getTopic());
        
        Debate debate = new Debate();
        debate.setTopic(config.getTopic());
        debate.setLanguage(config.getLanguage() != null ? config.getLanguage() : "en");
        debate.setTotalRounds(config.getRounds());
        debate.setStatus(Debate.DebateStatus.PENDING);
        
        debate = debateRepository.save(debate);
        
        // Create Agent A (Proponent)
        Agent agentA = new Agent();
        agentA.setName(config.getAgentA().getName());
        agentA.setStance(config.getAgentA().getStance());
        agentA.setPersonality(config.getAgentA().getPersonality());
        agentA.setRole(Agent.AgentRole.PROPONENT);
        agentA.setDebate(debate);
        agentRepository.save(agentA);
        
        // Create Agent B (Opponent)
        Agent agentB = new Agent();
        agentB.setName(config.getAgentB().getName());
        agentB.setStance(config.getAgentB().getStance());
        agentB.setPersonality(config.getAgentB().getPersonality());
        agentB.setRole(Agent.AgentRole.OPPONENT);
        agentB.setDebate(debate);
        agentRepository.save(agentB);
        
        debate.getAgents().add(agentA);
        debate.getAgents().add(agentB);
        
        log.info("Debate created with ID: {}", debate.getId());
        return debate;
    }
    
    @Transactional(readOnly = true)
    public Debate getDebate(Long id) {
        return debateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Debate not found: " + id));
    }
    
    @Transactional
    public void updateDebateStatus(Long debateId, Debate.DebateStatus status) {
        Debate debate = getDebate(debateId);
        debate.setStatus(status);
        debateRepository.save(debate);
    }
    
    @Transactional
    public DebateRound createRound(Debate debate, int roundNumber) {
        DebateRound round = new DebateRound();
        round.setDebate(debate);
        round.setRoundNumber(roundNumber);
        return roundRepository.save(round);
    }
    
    @Transactional
    public Message saveMessage(Agent agent, DebateRound round, String content, int sequence) {
        Message message = new Message();
        message.setAgent(agent);
        message.setRound(round);
        message.setContent(content);
        message.setSequenceNumber(sequence);
        return messageRepository.save(message);
    }
    
    @Transactional
    public void updateCurrentRound(Long debateId, int round) {
        Debate debate = getDebate(debateId);
        debate.setCurrentRound(round);
        debateRepository.save(debate);
    }
    
    @Transactional
    public void saveConclusion(Long debateId, String conclusion) {
        Debate debate = getDebate(debateId);
        debate.setConclusion(conclusion);
        debateRepository.save(debate);
    }
    
    @Transactional(readOnly = true)
    public HistoryResponse getDebateHistory(Long debateId) {
        Debate debate = getDebate(debateId);
        List<Message> messages = messageRepository.findByDebateOrderByRoundAndSequence(debate);
        return HistoryResponse.from(debate, messages);
    }
    
    @Transactional(readOnly = true)
    public List<Message> getAllMessages(Debate debate) {
        return messageRepository.findByDebateOrderByRoundAndSequence(debate);
    }
}
