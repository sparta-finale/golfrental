package com.golfRental.domain.chat.dto.request;


import jakarta.validation.constraints.NotNull;

public record ChatRoomCreateRequest(
        @NotNull(message = "예약 ID는 필수입니다.")
        Long reservationId
) {
}
