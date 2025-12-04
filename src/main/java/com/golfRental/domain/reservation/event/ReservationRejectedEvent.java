package com.golfRental.domain.reservation.event;

import com.golfRental.domain.reservation.entity.Reservation;

public class ReservationRejectedEvent extends ReservationEvent {
    public ReservationRejectedEvent(Reservation reservation) {
        super(reservation);
    }
}
