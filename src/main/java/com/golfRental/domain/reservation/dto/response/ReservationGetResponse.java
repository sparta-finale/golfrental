package com.golfRental.domain.reservation.dto.response;

import com.golfRental.domain.reservation.enums.ReservationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationGetResponse(
        Long reservationId,
        Long postId,
        Long hostUserId,
        Long guestUserId,
        LocalDateTime reservationStartAt,
        LocalDateTime reservationEndAt,
        Integer price,
        Integer deposit,
        ReservationStatus status
) {
}
