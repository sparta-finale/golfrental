package com.golfRental.domain.point.service.query;

import com.golfRental.domain.point.dto.response.PointBalanceResponse;

public interface PointQueryService {

    PointBalanceResponse getBalance(Long userId);
}
