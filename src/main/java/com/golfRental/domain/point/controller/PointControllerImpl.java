package com.golfRental.domain.point.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.point.dto.response.PointBalanceResponse;
import com.golfRental.domain.point.message.PointSuccessMessage;
import com.golfRental.domain.point.service.query.PointQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class PointControllerImpl implements PointController {

    private final PointQueryService pointQueryService;

    @Override
    @GetMapping("/balance")
    public ResponseEntity<CommonApiResponse<PointBalanceResponse>> getMyPointBalance(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        PointBalanceResponse response =
                pointQueryService.getBalance(authUser.getUserId());

        return CommonApiResponse.success(
                response,
                PointSuccessMessage.GET_POINT_BALANCE
        );
    }
}
