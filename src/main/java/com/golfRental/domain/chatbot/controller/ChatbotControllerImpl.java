package com.golfRental.domain.chatbot.controller;


import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.chatbot.dto.request.ChatbotMessageRequest;
import com.golfRental.domain.chatbot.dto.response.ChatbotMessageResponse;
import com.golfRental.domain.chatbot.service.command.ChatbotCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatbotControllerImpl implements ChatbotController {

    private final ChatbotCommandService chatbotCommandService;

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
        return CommonApiResponse.created(response, "챗봇 응답 성공");
    }
}
