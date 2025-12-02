package com.golfRental.domain.chat.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.chat.dto.request.ChatRoomCreateRequest;
import com.golfRental.domain.chat.dto.response.ChatRoomResponse;
import org.springframework.http.ResponseEntity;

public interface ChatController {
    ResponseEntity<CommonApiResponse<ChatRoomResponse>> createChatRoom(
            AuthUser authUser,
            ChatRoomCreateRequest request
    );
}
