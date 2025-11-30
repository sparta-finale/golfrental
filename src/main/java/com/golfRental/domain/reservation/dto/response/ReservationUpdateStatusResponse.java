package com.golfRental.domain.reservation.dto.response;

import com.golfRental.domain.reservation.enums.ReservationStatus;
import lombok.Builder;

@Builder
public record ReservationUpdateStatusResponse(
        Long reservationId,
        ReservationStatus status
) {
}
