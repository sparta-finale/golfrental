package com.golfRental.domain.chat.service.command;

import com.golfRental.domain.chat.dto.request.ChatRoomCreateRequest;
import com.golfRental.domain.chat.dto.response.ChatRoomResponse;
import com.golfRental.domain.chat.entity.ChatRoom;
import com.golfRental.domain.chat.exception.ChatErrorCode;
import com.golfRental.domain.chat.exception.ChatException;
import com.golfRental.domain.chat.repository.ChatRoomRepository;
import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.reservation.service.query.ReservationQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatCommandServiceImpl implements ChatCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final ReservationQueryService reservationQueryService;

    @Override
    @Transactional
    public ChatRoomResponse createChatRoom(Long currentUserId, ChatRoomCreateRequest request) {
        if (chatRoomRepository.existsByReservationId(request.getReservationId())) {
            throw new ChatException(ChatErrorCode.CHAT_ROOM_ALREADY_EXISTS);
        }

        Reservation reservation = reservationQueryService.findById(request.getReservationId());
        validateParticipant(reservation, currentUserId);

        ChatRoom chatRoom = ChatRoom.builder()
                .reservation(reservation)
                .hostUser(reservation.getHostUser())
                .guestUser(reservation.getGuestUser())
                .build();

        try {
            ChatRoom savedChatRoom = chatRoomRepository.saveAndFlush(chatRoom);
            return ChatRoomResponse.from(savedChatRoom);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.warn("채팅방 생성 중 경쟁 상태 발생 가능성. reservationId: {}", request.getReservationId(), e);
            throw new ChatException(ChatErrorCode.CHAT_ROOM_ALREADY_EXISTS);
        }
    }

    private void validateParticipant(Reservation reservation, Long currentUserId) {
        boolean isHost = reservation.getHostUser().getId().equals(currentUserId);
        boolean isGuest = reservation.getGuestUser().getId().equals(currentUserId);

        if (!isHost && !isGuest) {
            log.warn("채팅방 생성 권한 없음 - reservationId: {}, userId: {}",
                    reservation.getId(), currentUserId);
            throw new ChatException(ChatErrorCode.CHAT_ROOM_FORBIDDEN);
        }
    }
}
