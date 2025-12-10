package com.golfRental.domain.chatbot.repository;

import com.golfRental.domain.chatbot.entity.ChatHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    Slice<ChatHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<ChatHistory> findByUserIdAndCreatedAtBetweenOrderByCreatedAtAsc(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    long countByUserId(Long userId);

}
