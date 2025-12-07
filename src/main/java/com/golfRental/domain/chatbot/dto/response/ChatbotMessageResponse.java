package com.golfRental.domain.chatbot.dto.response;

import java.time.LocalDateTime;

public record ChatbotMessageResponse(
        String message,
        LocalDateTime timestamp
) {
    public static ChatbotMessageResponse of(String message) {
        return new ChatbotMessageResponse(message, LocalDateTime.now());
    }
}
