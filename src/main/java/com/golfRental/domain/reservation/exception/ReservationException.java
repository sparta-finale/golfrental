package com.golfRental.domain.reservation.exception;

import com.golfRental.common.exception.GlobalException;

public class ReservationException extends GlobalException {

    public ReservationException(ReservationErrorCode reservationErrorCode) {
        super(reservationErrorCode);
    }
}
