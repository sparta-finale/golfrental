package com.golfRental.domain.chatbot.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.chatbot.dto.request.ChatbotMessageRequest;
import com.golfRental.domain.chatbot.dto.response.ChatHistoryResponse;
import com.golfRental.domain.chatbot.dto.response.ChatbotMessageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ChatbotController {

    ResponseEntity<CommonApiResponse<ChatbotMessageResponse>> sendMessage(
            AuthUser authUser,
            ChatbotMessageRequest request
    );

    ResponseEntity<CommonApiResponse<SliceResponse<ChatHistoryResponse>>> getHistory(
            AuthUser authUser,
            Pageable pageable
    );
}
