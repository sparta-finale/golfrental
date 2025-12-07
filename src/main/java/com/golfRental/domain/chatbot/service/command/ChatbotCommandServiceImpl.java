package com.golfRental.domain.chatbot.service.command;

import com.golfRental.domain.chatbot.dto.response.ChatbotMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatbotCommandServiceImpl implements ChatbotCommandService {

    public ChatbotMessageResponse chat(Long userId, String message) {

        String chatbotAnswer = message;
        return ChatbotMessageResponse.of(chatbotAnswer);
    }
}
