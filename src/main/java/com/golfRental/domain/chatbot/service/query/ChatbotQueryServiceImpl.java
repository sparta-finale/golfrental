package com.golfRental.domain.chatbot.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.chatbot.dto.response.ChatHistoryResponse;
import com.golfRental.domain.chatbot.entity.ChatHistory;
import com.golfRental.domain.chatbot.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatbotQueryServiceImpl implements ChatbotQueryService {

    private final ChatHistoryRepository chatHistoryRepository;

    @Override
    public SliceResponse<ChatHistoryResponse> getChatHistory(Long userId, Pageable pageable) {

        Slice<ChatHistory> slice = chatHistoryRepository
                .findByUserIdOrderByCreatedAtDesc(userId, pageable);

        Slice<ChatHistoryResponse> responseSlice = slice.map(ChatHistoryResponse::from);

        return SliceResponse.fromSlice(responseSlice);
    }
}
