package com.golfRental.domain.point.service.command;

import com.golfRental.domain.point.dto.response.PointBalanceResponse;
import com.golfRental.domain.point.dto.response.PointUseResponse;

public interface PointCommandService {

    PointBalanceResponse getBalance(Long userId);

    PointUseResponse usePoints(Long userId, Integer amount);
}
