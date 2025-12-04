package com.golfRental.domain.reservation.event;

import com.golfRental.domain.reservation.entity.Reservation;

public class ReservationCancelledEvent extends ReservationEvent {
    public ReservationCancelledEvent(Reservation reservation) {
        super(reservation);
    }
}
