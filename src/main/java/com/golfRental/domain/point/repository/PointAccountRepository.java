package com.golfRental.domain.point.repository;

import com.golfRental.domain.point.entity.PointAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointAccountRepository extends JpaRepository<PointAccount, Long> {
}
