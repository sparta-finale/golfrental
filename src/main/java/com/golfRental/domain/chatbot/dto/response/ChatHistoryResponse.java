package com.golfRental.domain.chatbot.dto.response;

import com.golfRental.domain.chatbot.entity.ChatHistory;
import com.golfRental.domain.chatbot.enums.ChatRole;

import java.time.LocalDateTime;

public record ChatHistoryResponse(
        Long id,
        ChatRole role,
        String message,
        LocalDateTime createdAt
) {
    public static ChatHistoryResponse from(ChatHistory chatHistory) {
        return new ChatHistoryResponse(
                chatHistory.getId(),
                chatHistory.getRole(),
                chatHistory.getMessage(),
                chatHistory.getCreatedAt()
        );
    }
}
