package com.golfRental.domain.payment.service.command;

import com.golfRental.domain.payment.dto.request.PaymentConfirmRequest;
import com.golfRental.domain.payment.dto.response.PaymentConfirmResponse;
import com.golfRental.domain.payment.dto.response.TossConfirmResponse;
import com.golfRental.domain.payment.entity.Payment;
import com.golfRental.domain.payment.enums.PaymentStatus;
import com.golfRental.domain.payment.exception.PaymentErrorCode;
import com.golfRental.domain.payment.exception.PaymentException;
import com.golfRental.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final WebClient tossWebClient;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request, Long userId) {

        TossConfirmResponse tossResponse = tossWebClient.post()
                .uri("/confirm")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(
                                        new PaymentException(PaymentErrorCode.PAYMENT_VALIDATION_FAILED)
                                ))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(
                                        new PaymentException(PaymentErrorCode.PAYMENT_SERVER_ERROR)
                                ))
                )
                .bodyToMono(TossConfirmResponse.class)
                .block();

        // ---- 결제 정보 저장 ----
        Payment payment = Payment.createSuccessWithoutUser(
                tossResponse.paymentKey(),
                tossResponse.orderId(),
                tossResponse.totalAmount()
        );

        Payment savedPayment = paymentRepository.save(payment);

        return PaymentConfirmResponse.from(savedPayment);
    }
}
