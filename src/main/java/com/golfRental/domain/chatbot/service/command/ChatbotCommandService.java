package com.golfRental.domain.chatbot.service.command;

import com.golfRental.domain.chatbot.dto.response.ChatbotMessageResponse;

public interface ChatbotCommandService {

    ChatbotMessageResponse chat(Long userId, String message);
}
