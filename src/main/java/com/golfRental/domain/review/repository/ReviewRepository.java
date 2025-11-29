package com.golfRental.domain.review.repository;

import com.golfRental.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 예약 ID로 리뷰 존재 여부 확인
     */
    boolean existsByReservationId(Long reservationId);
}