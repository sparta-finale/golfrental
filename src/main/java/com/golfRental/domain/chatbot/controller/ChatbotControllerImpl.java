package com.golfRental.domain.chatbot.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.chatbot.dto.request.ChatbotMessageRequest;
import com.golfRental.domain.chatbot.dto.response.ChatHistoryResponse;
import com.golfRental.domain.chatbot.dto.response.ChatbotMessageResponse;
import com.golfRental.domain.chatbot.service.command.ChatbotCommandService;
import com.golfRental.domain.chatbot.service.query.ChatbotQueryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatbotControllerImpl implements ChatbotController {

    private final ChatbotCommandService chatbotCommandService;
    private final ChatbotQueryService chatbotQueryService;


    @Override
    @PostMapping("/messages")
    public ResponseEntity<CommonApiResponse<ChatbotMessageResponse>> sendMessage(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid ChatbotMessageRequest request
    ) {
        ChatbotMessageResponse response = chatbotCommandService.chat(
                authUser.getUserId(),
                request.message()
        );
        return CommonApiResponse.success(response, "챗봇 응답 성공");
    }

    @Override
    @Operation(summary = "대화 히스토리 조회", description = "사용자의 챗봇 대화 히스토리를 조회합니다")
    @GetMapping("/history")
    public ResponseEntity<CommonApiResponse<Page<ChatHistoryResponse>>> getHistory(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        log.info("대화 히스토리 조회 요청 - userId: {}, page: {}, size: {}",
                authUser.getUserId(), pageable.getPageNumber(), pageable.getPageSize());

        Page<ChatHistoryResponse> history = chatbotQueryService.getChatHistory(
                authUser.getUserId(),
                pageable
        );

        return CommonApiResponse.success(history, "대화 히스토리 조회 성공");
    }
}
