package com.golfRental.domain.chatbot.service.query;

import com.golfRental.domain.chatbot.dto.response.ChatHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatbotQueryService {

    Page<ChatHistoryResponse> getChatHistory(Long userId, Pageable pageable);

}
