package com.golfRental.domain.chat.repository;

import com.golfRental.domain.chat.entity.ChatMessage;
import com.golfRental.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm FROM ChatMessage cm " +
            "JOIN FETCH cm.sender " +
            "WHERE cm.chatRoom = :chatRoom " +
            "AND cm.deletedAt IS NULL " +
            "ORDER BY cm.createdAt ASC")
    Slice<ChatMessage> findByChatRoomOrderByCreatedAtAsc(@Param("chatRoom") ChatRoom chatRoom, Pageable pageable);
}
