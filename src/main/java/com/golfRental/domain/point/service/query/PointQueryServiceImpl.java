package com.golfRental.domain.point.service.query;


import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.point.dto.response.PointTransactionGetResponse;
import com.golfRental.domain.point.entity.PointTransaction;
import com.golfRental.domain.point.repository.PointTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointQueryServiceImpl implements PointQueryService {

    private final PointTransactionRepository transactionRepository;

    @Override
    public SliceResponse<PointTransactionGetResponse> getMyTransactions(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Slice<PointTransaction> transactions =
                transactionRepository.findByUserId(userId, pageable);

        Slice<PointTransactionGetResponse> contents = transactions.map(transaction ->
                PointTransactionGetResponse.builder()
                        .transactionId(transaction.getId())
                        .amount(transaction.getAmount())
                        .type(transaction.getType())
                        .balanceAfter(transaction.getBalanceAfter())
                        .createdAt(transaction.getCreatedAt())
                        .build()
        );

        return SliceResponse.fromSlice(contents);
    }
}