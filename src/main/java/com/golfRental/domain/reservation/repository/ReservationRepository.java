package com.golfRental.domain.reservation.repository;

import com.golfRental.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
            "WHERE r.post.id = :postId " +
            "AND r.deletedAt IS NULL " +
            "AND r.status IN ('REQUESTED', 'APPROVED', 'RENTED', 'RETURNING') " +
            "AND NOT (r.reservationEndAt <= :startAt OR r.reservationStartAt >= :endAt)")
    boolean existsConflictingReservation(
            @Param("postId") Long postId,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt
    );

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.post " +
            "JOIN FETCH r.hostUser " +
            "JOIN FETCH r.guestUser " +
            "WHERE r.post.id = :postId " +
            "AND r.reservationEndAt >= CURRENT_TIMESTAMP " +
            "AND r.deletedAt IS NULL")
    Slice<Reservation> findByPostId(@Param("postId") Long postId, Pageable pageable);
}
