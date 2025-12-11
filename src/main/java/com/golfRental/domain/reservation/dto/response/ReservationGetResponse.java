package com.golfRental.domain.reservation.dto.response;

import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.reservation.enums.ReservationStatus;

import java.time.LocalDateTime;

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

    public static ReservationGetResponse from(Reservation reservation) {
        return new ReservationGetResponse(
                reservation.getId(),
                reservation.getPost().getId(),
                reservation.getHostUser().getId(),
                reservation.getGuestUser().getId(),
                reservation.getReservationStartAt(),
                reservation.getReservationEndAt(),
                reservation.getPrice(),
                reservation.getDeposit(),
                reservation.getStatus()
        );
    }
}