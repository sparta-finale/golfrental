package com.golfRental.domain.review.repository;

import com.golfRental.domain.review.entity.Review;
import com.golfRental.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 예약 ID로 리뷰 존재 여부 확인
     */
    boolean existsByReservationId(Long reservationId);

    /**
     * 리뷰 단건 조회 (user, targetUser, reservation fetch join)
     */
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.user " +
            "JOIN FETCH r.targetUser " +
            "JOIN FETCH r.reservation " +
            "WHERE r.id = :reviewId")
    Optional<Review> findByIdWithDetails(@Param("reviewId") Long reviewId);

    /**
     * 특정 유저가 받은 리뷰 목록 조회 (최신순, user fetch join)
     */
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.user " +
            "JOIN FETCH r.reservation " +
            "WHERE r.targetUser = :targetUser")
    Slice<Review> findByTargetUser(
            @Param("targetUser") User targetUser,
            Pageable pageable
    );
}