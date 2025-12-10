package com.golfRental.domain.chatbot.repository;

import com.golfRental.domain.chatbot.entity.ChatHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    Page<ChatHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT ch FROM ChatHistory ch WHERE ch.user.id = :userId ORDER BY ch.createdAt DESC")
    List<ChatHistory> findRecentByUserId(@Param("userId") Long userId, Pageable pageable);

    List<ChatHistory> findByUserIdAndCreatedAtBetweenOrderByCreatedAtAsc(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    long countByUserId(Long userId);

}
