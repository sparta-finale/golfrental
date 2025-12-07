package com.golfRental.domain.chat.dto.event;

import com.golfRental.domain.chat.dto.response.ChatMessageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long chatRoomId;
    private ChatMessageResponse message;

    public static ChatMessageEvent of(Long chatRoomId, ChatMessageResponse message) {
        return new ChatMessageEvent(chatRoomId, message);
    }
}
