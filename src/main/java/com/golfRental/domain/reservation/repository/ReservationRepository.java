package com.golfRental.domain.reservation.repository;

import com.golfRental.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.post " +
            "JOIN FETCH r.hostUser " +
            "JOIN FETCH r.guestUser " +
            "WHERE r.id = :reservationId " +
            "AND r.deletedAt IS NULL")
    Optional<Reservation> findByIdWithDetails(@Param("reservationId") Long reservationId);

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.post " +
            "JOIN FETCH r.hostUser " +
            "JOIN FETCH r.guestUser " +
            "WHERE (r.hostUser.id = :userId OR r.guestUser.id = :userId) " +
            "AND r.deletedAt IS NULL")
    Slice<Reservation> findMyReservations(
            @Param("userId") Long userId,
            Pageable pageable
    );
}
