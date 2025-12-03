package com.golfRental.domain.point.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.point.dto.response.PointBalanceResponse;
import org.springframework.http.ResponseEntity;

public interface PointController {

    ResponseEntity<CommonApiResponse<PointBalanceResponse>> getMyPointBalance(
            AuthUser authUser
    );
}
