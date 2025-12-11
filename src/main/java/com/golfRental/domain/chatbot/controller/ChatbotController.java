package com.golfRental.domain.chatbot.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.chatbot.dto.request.ChatbotMessageRequest;
import com.golfRental.domain.chatbot.dto.response.ChatHistoryResponse;
import com.golfRental.domain.chatbot.dto.response.ChatbotMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "챗봇 관리", description = "챗봇 관련 API")
public interface ChatbotController {

    @Operation(
            summary = "챗봇메세지 발송",
            description = "챗봇 답변을 합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ChatbotMessageResponse>> sendMessage(
            AuthUser authUser,
            ChatbotMessageRequest request
    );

    @Operation(
            summary = "챗봇 조회",
            description = "챗봇메세지를 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<ChatHistoryResponse>>> getHistory(
            AuthUser authUser,
            Pageable pageable
    );
}
