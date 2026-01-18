package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 2000)
    private String stance;
    
    @Column(length = 2000)
    private String personality;
    
    @Enumerated(EnumType.STRING)
    private AgentRole role;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debate_id")
    @JsonIgnore
    private Debate debate;
    
    public enum AgentRole {
        PROPONENT, OPPONENT
    }
}
