package com.golfRental.domain.chat.dto.response;

import com.golfRental.domain.chat.entity.ChatRoom;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomResponse(
        Long chatRoomId,
        Long reservationId,
        Long hostUserId,
        String hostUsername,
        Long guestUserId,
        String guestUsername,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .reservationId(chatRoom.getReservation().getId())
                .hostUserId(chatRoom.getHostUser().getId())
                .hostUsername(chatRoom.getHostUser().getUsername())
                .guestUserId(chatRoom.getGuestUser().getId())
                .guestUsername(chatRoom.getGuestUser().getUsername())
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
    }
}