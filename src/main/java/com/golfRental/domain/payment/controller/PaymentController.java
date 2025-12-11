package com.golfRental.domain.payment.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.payment.dto.request.PaymentConfirmRequest;
import com.golfRental.domain.payment.dto.response.PaymentConfirmResponse;
import com.golfRental.domain.payment.dto.response.PaymentHistoryResponse;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;

public interface PaymentController {

    ResponseEntity<CommonApiResponse<PaymentConfirmResponse>> confirmPayment(
            PaymentConfirmRequest request,
            AuthUser authUser
    );

    ResponseEntity<CommonApiResponse<Slice<PaymentHistoryResponse>>> getMyPayments(
            AuthUser authUser,
            int page,
            int size
    );
}
