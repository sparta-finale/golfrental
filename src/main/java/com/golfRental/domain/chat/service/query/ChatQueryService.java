package com.golfRental.domain.chat.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.chat.dto.response.ChatRoomResponse;
import org.springframework.data.domain.Pageable;

public interface ChatQueryService {

    SliceResponse<ChatRoomResponse> getMyChatRooms(Long currentUserId, Pageable pageable);

}
