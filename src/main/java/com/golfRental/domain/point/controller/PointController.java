package com.golfRental.domain.point.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.point.dto.request.PointUseRequest;
import com.golfRental.domain.point.dto.response.PointBalanceResponse;
import com.golfRental.domain.point.dto.response.PointTransactionGetResponse;
import com.golfRental.domain.point.dto.response.PointUseResponse;
import org.springframework.http.ResponseEntity;

public interface PointController {

    ResponseEntity<CommonApiResponse<PointBalanceResponse>> getMyPointBalance(
            AuthUser authUser);

    ResponseEntity<CommonApiResponse<SliceResponse<PointTransactionGetResponse>>> getMyTransactions(
            AuthUser authUser,
            int page,
            int size
    );

    ResponseEntity<CommonApiResponse<PointUseResponse>> usePoints(
            AuthUser authUser,
            PointUseRequest request
    );
}
