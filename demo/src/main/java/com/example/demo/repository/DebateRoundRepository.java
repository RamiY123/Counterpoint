package com.example.demo.repository;

import com.example.demo.model.DebateRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebateRoundRepository extends JpaRepository<DebateRound, Long> {
}
