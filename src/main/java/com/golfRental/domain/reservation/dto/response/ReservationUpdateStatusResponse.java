package com.golfRental.domain.reservation.dto.response;

import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.reservation.enums.ReservationStatus;

public record ReservationUpdateStatusResponse(
        Long reservationId,
        ReservationStatus status
) {

    public static ReservationUpdateStatusResponse from(Reservation reservation) {
        return new ReservationUpdateStatusResponse(
                reservation.getId(),
                reservation.getStatus()
        );
    }
}