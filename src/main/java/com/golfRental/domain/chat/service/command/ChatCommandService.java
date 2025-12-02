package com.golfRental.domain.chat.service.command;

import com.golfRental.domain.chat.dto.request.ChatMessageCreateRequest;
import com.golfRental.domain.chat.dto.request.ChatRoomCreateRequest;
import com.golfRental.domain.chat.dto.response.ChatMessageResponse;
import com.golfRental.domain.chat.dto.response.ChatRoomResponse;

public interface ChatCommandService {

    ChatRoomResponse createChatRoom(Long currentUserId, ChatRoomCreateRequest request);

    ChatMessageResponse sendMessage(Long currentUserId, Long chatRoomId, ChatMessageCreateRequest request);

}
