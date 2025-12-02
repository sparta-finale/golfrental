package com.golfRental.domain.chat.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.chat.dto.request.ChatRoomCreateRequest;
import com.golfRental.domain.chat.dto.response.ChatRoomResponse;
import com.golfRental.domain.chat.service.command.ChatCommandService;
import com.golfRental.domain.chat.service.query.ChatQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatControllerImpl implements ChatController {

    private final ChatCommandService chatCommandService;
    private final ChatQueryService chatQueryService;


    @Override
    @PostMapping("/rooms")
    public ResponseEntity<CommonApiResponse<ChatRoomResponse>> createChatRoom(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody ChatRoomCreateRequest request
    ) {
        ChatRoomResponse response = chatCommandService.createChatRoom(authUser.getUserId(), request);
        return CommonApiResponse.created(response, "채팅방 생성 성공");
    }

    @Override
    @GetMapping("/rooms")
    public ResponseEntity<CommonApiResponse<SliceResponse<ChatRoomResponse>>> getMyChatRooms(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        SliceResponse<ChatRoomResponse> response = chatQueryService.getMyChatRooms(authUser.getUserId(), pageable);
        return CommonApiResponse.success(response, "채팅방 목록 조회 성공");
    }
}
