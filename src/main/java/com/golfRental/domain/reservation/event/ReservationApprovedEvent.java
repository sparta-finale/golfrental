package com.golfRental.domain.reservation.event;

import com.golfRental.domain.reservation.entity.Reservation;

public class ReservationApprovedEvent extends ReservationEvent {
    public ReservationApprovedEvent(Reservation reservation) {
        super(reservation);
    }
}
