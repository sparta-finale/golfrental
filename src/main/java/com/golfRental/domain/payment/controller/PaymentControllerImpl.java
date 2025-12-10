package com.golfRental.domain.payment.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.payment.dto.request.PaymentConfirmRequest;
import com.golfRental.domain.payment.dto.response.PaymentConfirmResponse;
import com.golfRental.domain.payment.message.PaymentSuccessMessage;
import com.golfRental.domain.payment.service.command.PaymentCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentControllerImpl implements PaymentController {

    private final PaymentCommandService paymentCommandService;

    @Override
    @PostMapping("/confirm")
    public ResponseEntity<CommonApiResponse<PaymentConfirmResponse>> confirmPayment(
            @Valid @RequestBody PaymentConfirmRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {

        PaymentConfirmResponse response =
                paymentCommandService.confirmPayment(request, authUser.getUserId());

        return CommonApiResponse.success(
                response,
                PaymentSuccessMessage.PAYMENT_CONFIRMED
        );
    }
}
