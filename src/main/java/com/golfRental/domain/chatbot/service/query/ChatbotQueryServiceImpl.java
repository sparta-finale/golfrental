package com.golfRental.domain.chatbot.service.query;

import com.golfRental.domain.chatbot.dto.response.ChatHistoryResponse;
import com.golfRental.domain.chatbot.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatbotQueryServiceImpl implements ChatbotQueryService {

    private final ChatHistoryRepository chatHistoryRepository;

    @Override
    public Page<ChatHistoryResponse> getChatHistory(Long userId, Pageable pageable) {

        return chatHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(ChatHistoryResponse::from);
    }
}
