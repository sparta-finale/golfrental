package com.golfRental.domain.payment.service.query;

import com.golfRental.domain.payment.dto.response.PaymentHistoryResponse;
import com.golfRental.domain.payment.entity.Payment;
import com.golfRental.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final PaymentRepository paymentRepository;

    @Override
    public Slice<PaymentHistoryResponse> getMyPayments(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<Payment> payments = paymentRepository
                .findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return payments.map(PaymentHistoryResponse::from);
    }
}