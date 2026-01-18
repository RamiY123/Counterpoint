package com.example.demo.repository;

import com.example.demo.model.Message;
import com.example.demo.model.Debate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Query("SELECT m FROM Message m WHERE m.round.debate = :debate ORDER BY m.round.roundNumber, m.sequenceNumber")
    List<Message> findByDebateOrderByRoundAndSequence(Debate debate);
}
