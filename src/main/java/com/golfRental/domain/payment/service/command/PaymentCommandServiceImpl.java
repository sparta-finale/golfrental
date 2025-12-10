package com.golfRental.domain.payment.service.command;

import com.golfRental.domain.payment.dto.request.PaymentConfirmRequest;
import com.golfRental.domain.payment.dto.response.PaymentConfirmResponse;
import com.golfRental.domain.payment.dto.response.TossConfirmResponse;
import com.golfRental.domain.payment.entity.Payment;
import com.golfRental.domain.payment.exception.PaymentErrorCode;
import com.golfRental.domain.payment.exception.PaymentException;
import com.golfRental.domain.payment.repository.PaymentRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request, Long userId) {

        // ---- Toss 서버 결제 승인 요청 ----
        TossConfirmResponse tossResponse = tossWebClient.post()
                .uri("/confirm")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("Toss Payments 4xx error response: {}", body);
                                    return Mono.error(new PaymentException(PaymentErrorCode.PAYMENT_VALIDATION_FAILED));
                                })
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("Toss Payments 5xx error response: {}", body);
                                    return Mono.error(new PaymentException(PaymentErrorCode.PAYMENT_SERVER_ERROR));
                                })
                )
                .bodyToMono(TossConfirmResponse.class)
                .block();

        // ---- 유저 조회 ----
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_VALIDATION_FAILED));

        // ---- 결제 금액 검증 ----
        if (!request.amount().equals(tossResponse.totalAmount())) {
            throw new PaymentException(PaymentErrorCode.INVALID_PAYMENT_AMOUNT);
        }

        // ---- 결제 정보 저장 ----
        Payment payment = Payment.createSuccess(
                tossResponse.paymentKey(),
                tossResponse.orderId(),
                tossResponse.totalAmount(),
                user
        );

        Payment savedPayment = paymentRepository.save(payment);

        return PaymentConfirmResponse.from(savedPayment);
    }
}