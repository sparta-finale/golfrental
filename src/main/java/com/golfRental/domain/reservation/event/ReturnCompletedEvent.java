package com.golfRental.domain.reservation.event;

import com.golfRental.domain.reservation.entity.Reservation;

public class ReturnCompletedEvent extends ReservationEvent {
    public ReturnCompletedEvent(Reservation reservation) {
        super(reservation);
    }
}
