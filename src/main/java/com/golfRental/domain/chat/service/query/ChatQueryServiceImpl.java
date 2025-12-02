package com.golfRental.domain.chat.service.query;


import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.chat.dto.response.ChatRoomResponse;
import com.golfRental.domain.chat.entity.ChatRoom;
import com.golfRental.domain.chat.repository.ChatRoomRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryServiceImpl implements ChatQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserQueryService userQueryService;

    public SliceResponse<ChatRoomResponse> getMyChatRooms(Long currentUserId, Pageable pageable) {

        User user = userQueryService.findById(currentUserId);
        Slice<ChatRoom> chatRooms = chatRoomRepository.findByParticipantOrderByUpdatedAtDesc(user, pageable);
        Slice<ChatRoomResponse> content = chatRooms.map(ChatRoomResponse::from);

        return SliceResponse.fromSlice(content);
    }
}
