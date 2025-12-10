package com.golfRental.domain.payment.repository;

import com.golfRental.domain.payment.entity.Payment;
import com.golfRental.domain.payment.repository.projection.PaymentHistoryProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(String orderId);

    Optional<Payment> findByPaymentKey(String paymentKey);

    @Query("""
            SELECT p.id AS paymentId,
                   p.amount AS amount,
                   p.status AS status,
                   p.method AS method,
                   p.createdAt AS createdAt
            FROM Payment p
            WHERE p.user.id = :userId
            ORDER BY p.createdAt DESC
            """)
    Slice<PaymentHistoryProjection> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
