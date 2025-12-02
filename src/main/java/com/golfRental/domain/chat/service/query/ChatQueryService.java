package com.golfRental.domain.chat.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.chat.dto.response.ChatMessageResponse;
import com.golfRental.domain.chat.dto.response.ChatRoomResponse;
import com.golfRental.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Pageable;

public interface ChatQueryService {

    SliceResponse<ChatRoomResponse> getMyChatRooms(Long currentUserId, Pageable pageable);

    ChatRoomResponse getChatRoom(Long currentUserId, Long chatRoomId);

    ChatRoom findById(Long chatRoomId);

    SliceResponse<ChatMessageResponse> getMessages(Long currentUserId, Long chatRoomId, Pageable pageable);
}
