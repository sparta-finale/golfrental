package com.golfRental.domain.chat.dto.event;

import com.golfRental.domain.chat.dto.response.ChatMessageResponse;

import java.io.Serializable;

public record ChatMessageEvent(
        Long chatRoomId,
        ChatMessageResponse message
) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static ChatMessageEvent of(Long chatRoomId, ChatMessageResponse message) {
        return new ChatMessageEvent(chatRoomId, message);
    }
}
