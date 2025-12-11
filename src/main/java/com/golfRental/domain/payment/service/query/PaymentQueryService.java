package com.golfRental.domain.payment.service.query;

import com.golfRental.domain.payment.dto.response.PaymentHistoryResponse;
import org.springframework.data.domain.Slice;

public interface PaymentQueryService {
    Slice<PaymentHistoryResponse> getMyPayments(Long userId, int page, int size);
}