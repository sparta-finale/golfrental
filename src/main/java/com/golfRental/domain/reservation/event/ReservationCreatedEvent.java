package com.golfRental.domain.reservation.event;

import com.golfRental.domain.reservation.entity.Reservation;

public class ReservationCreatedEvent extends ReservationEvent {
    public ReservationCreatedEvent(Reservation reservation) {
        super(reservation);
    }
}
