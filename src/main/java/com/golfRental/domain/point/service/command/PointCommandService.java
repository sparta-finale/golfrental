package com.golfRental.domain.point.service.command;

import com.golfRental.domain.point.dto.response.PointBalanceResponse;

public interface PointCommandService {

    PointBalanceResponse getBalance(Long userId);
}
