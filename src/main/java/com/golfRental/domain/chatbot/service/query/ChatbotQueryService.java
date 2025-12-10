package com.golfRental.domain.chatbot.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.chatbot.dto.response.ChatHistoryResponse;
import org.springframework.data.domain.Pageable;

public interface ChatbotQueryService {

    SliceResponse<ChatHistoryResponse> getChatHistory(Long userId, Pageable pageable);

}
