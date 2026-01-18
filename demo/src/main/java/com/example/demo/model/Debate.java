package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "debates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Debate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String topic;
    
    @Column(nullable = false)
    private String language = "en";
    
    @Enumerated(EnumType.STRING)
    private DebateStatus status = DebateStatus.PENDING;
    
    private int totalRounds = 3;
    private int currentRound = 0;
    
    @Column(length = 4000)
    private String conclusion;
    
    @OneToMany(mappedBy = "debate", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Agent> agents = new ArrayList<>();
    
    @OneToMany(mappedBy = "debate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DebateRound> rounds = new ArrayList<>();
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public enum DebateStatus {
        PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
