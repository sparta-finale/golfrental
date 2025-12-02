package com.golfRental.domain.chat.repository;

import com.golfRental.domain.chat.entity.ChatRoom;
import com.golfRental.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByReservationId(Long reservationId);

    Optional<ChatRoom> findByReservationId(Long reservationId);

    @Query("SELECT cr FROM ChatRoom cr " +
            "JOIN FETCH cr.reservation " +
            "JOIN FETCH cr.hostUser " +
            "JOIN FETCH cr.guestUser " +
            "WHERE (cr.hostUser = :user OR cr.guestUser = :user) " +
            "AND cr.deletedAt IS NULL " +
            "ORDER BY cr.updatedAt DESC")
    Slice<ChatRoom> findByParticipantOrderByUpdatedAtDesc(@Param("user") User user, Pageable pageable);

    @Query("SELECT cr FROM ChatRoom cr " +
            "JOIN FETCH cr.reservation " +
            "JOIN FETCH cr.hostUser " +
            "JOIN FETCH cr.guestUser " +
            "WHERE cr.id = :chatRoomId")
    Optional<ChatRoom> findByIdWithDetails(@Param("chatRoomId") Long chatRoomId);

    @Query("SELECT cr FROM ChatRoom cr " +
            "JOIN FETCH cr.reservation " +
            "JOIN FETCH cr.hostUser " +
            "JOIN FETCH cr.guestUser " +
            "WHERE cr.reservation.id = :reservationId")
    Optional<ChatRoom> findByReservationIdWithDetails(@Param("reservationId") Long reservationId);

    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.hostUser JOIN FETCH cr.guestUser WHERE cr.id = :chatRoomId")
    Optional<ChatRoom> findByIdWithUsers(@Param("chatRoomId") Long chatRoomId);
}