package com.golfRental.domain.point.repository;

import com.golfRental.domain.point.entity.PointTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {

    @Query("""
            SELECT t
            FROM PointTransaction t
            JOIN FETCH t.user u
            WHERE u.id = :userId
            AND t.deletedAt IS NULL
            ORDER BY t.id DESC
            """)
    Slice<PointTransaction> findByUserId(Long userId, Pageable pageable);
}
