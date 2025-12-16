package com.golfRental.domain.chat.dto.event;

import com.golfRental.domain.chat.dto.response.ChatMessageResponse;

public record ChatMessageEvent(
        Long chatRoomId,
        ChatMessageResponse message
) {
    public static ChatMessageEvent of(Long chatRoomId, ChatMessageResponse message) {
        return new ChatMessageEvent(chatRoomId, message);
    }
}
