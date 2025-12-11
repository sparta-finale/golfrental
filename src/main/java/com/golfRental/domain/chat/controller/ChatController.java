package com.golfRental.domain.chat.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.chat.dto.request.ChatMessageCreateRequest;
import com.golfRental.domain.chat.dto.request.ChatRoomCreateRequest;
import com.golfRental.domain.chat.dto.response.ChatMessageResponse;
import com.golfRental.domain.chat.dto.response.ChatRoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "채팅 관리", description = "채팅 관련 API")
public interface ChatController {

    @Operation(
            summary = "채팅방 생성",
            description = "채팅방을 생성합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ChatRoomResponse>> createChatRoom(
            AuthUser authUser,
            ChatRoomCreateRequest request
    );

    @Operation(
            summary = "채팅방 조회",
            description = "나의 채팅방을 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<ChatRoomResponse>>> getMyChatRooms(
            AuthUser authUser,
            Pageable pageable
    );

    @Operation(
            summary = "채팅방 조회",
            description = "채팅방을 상세조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ChatRoomResponse>> getChatRoom(
            AuthUser authUser,
            Long chatRoomId
    );


    @Operation(
            summary = "메세지 전송",
            description = "메세지를 전송합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ChatMessageResponse>> sendMessage(
            AuthUser authUser,
            Long chatRoomId,
            ChatMessageCreateRequest request
    );

    @Operation(
            summary = "메세지 조회",
            description = "메세지를 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<ChatMessageResponse>>> getMessages(
            AuthUser authUser,
            Long chatRoomId,
            Pageable pageable
    );
}
