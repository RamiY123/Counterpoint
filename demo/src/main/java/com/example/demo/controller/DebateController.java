package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Debate;
import com.example.demo.service.DebateService;
import com.example.demo.service.DebateOrchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/debate")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
@Slf4j
public class DebateController {
    
    private final DebateService debateService;
    private final DebateOrchestrator orchestrator;
    
    @PostMapping("/configure")
    public ResponseEntity<SessionResponse> configureDebate(@RequestBody ConfigRequest config) {
        log.info("Configuring debate with topic: {}", config.getTopic());
        Debate debate = debateService.createDebate(config);
        return ResponseEntity.ok(SessionResponse.from(debate));
    }
    
    @PostMapping("/{debateId}/start")
    public ResponseEntity<String> startDebate(@PathVariable Long debateId) {
        log.info("Starting debate: {}", debateId);
        orchestrator.startDebate(debateId);
        return ResponseEntity.ok("Debate started");
    }
    
    @GetMapping("/{debateId}")
    public ResponseEntity<HistoryResponse> getDebate(@PathVariable Long debateId) {
        log.info("Getting debate: {}", debateId);
        return ResponseEntity.ok(debateService.getDebateHistory(debateId));
    }
    
    @GetMapping("/{debateId}/status")
    public ResponseEntity<SessionResponse> getDebateStatus(@PathVariable Long debateId) {
        Debate debate = debateService.getDebate(debateId);
        return ResponseEntity.ok(SessionResponse.from(debate));
    }
}
