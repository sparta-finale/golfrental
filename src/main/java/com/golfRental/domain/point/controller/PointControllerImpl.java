package com.golfRental.domain.point.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.point.dto.request.PointUseRequest;
import com.golfRental.domain.point.dto.response.PointBalanceResponse;
import com.golfRental.domain.point.dto.response.PointTransactionGetResponse;
import com.golfRental.domain.point.dto.response.PointUseResponse;
import com.golfRental.domain.point.message.PointSuccessMessage;
import com.golfRental.domain.point.service.command.PointCommandService;
import com.golfRental.domain.point.service.query.PointQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class PointControllerImpl implements PointController {

    private final PointCommandService pointCommandService;
    private final PointQueryService pointQueryService;

    @Override
    @GetMapping("/balance")
    public ResponseEntity<CommonApiResponse<PointBalanceResponse>> getMyPointBalance(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        PointBalanceResponse response = pointCommandService.getBalance(authUser.getUserId());

        return CommonApiResponse.success(
                response,
                PointSuccessMessage.GET_POINT_BALANCE
        );
    }

    @Override
    @GetMapping("/transactions")
    public ResponseEntity<CommonApiResponse<SliceResponse<PointTransactionGetResponse>>> getMyTransactions(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        SliceResponse<PointTransactionGetResponse> response = pointQueryService.getMyTransactions(
                authUser.getUserId(),
                page,
                size
        );

        return CommonApiResponse.success(
                response,
                PointSuccessMessage.POINT_TRANSACTION_LIST
        );
    }

    @Override
    @PostMapping("/use")
    public ResponseEntity<CommonApiResponse<PointUseResponse>> usePoints(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody PointUseRequest request
    ) {

        PointUseResponse response = pointCommandService.usePoints(
                authUser.getUserId(),
                request.getAmount()
        );

        return CommonApiResponse.success(
                response,
                PointSuccessMessage.POINT_USED
        );
    }
}
