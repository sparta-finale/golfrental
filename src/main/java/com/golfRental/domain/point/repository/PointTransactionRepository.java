package com.golfRental.domain.point.repository;

import com.golfRental.domain.point.entity.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
}
