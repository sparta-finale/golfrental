package com.golfRental.domain.point.service.query;


import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.point.dto.response.PointTransactionGetResponse;
import com.golfRental.domain.point.entity.PointAccount;
import com.golfRental.domain.point.entity.PointTransaction;
import com.golfRental.domain.point.repository.PointAccountRepository;
import com.golfRental.domain.point.repository.PointTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointQueryServiceImpl implements PointQueryService {


    private final PointTransactionRepository transactionRepository;
    private final PointAccountRepository pointAccountRepository;

    @Override
    public SliceResponse<PointTransactionGetResponse> getMyTransactions(Long userId, int page, int size) {

        // 계좌가 없으면 빈 SliceResponse 반환
        PointAccount account = pointAccountRepository.findByUserId(userId)
                .orElse(null);

        if (account == null) {
            Slice<PointTransactionGetResponse> emptySlice =
                    new PageImpl<>(List.of(), PageRequest.of(page, size), 0);
            return SliceResponse.fromSlice(emptySlice);
        }

        Pageable pageable = PageRequest.of(page, size);

        Slice<PointTransaction> transactions =
                transactionRepository.findByUserId(userId, pageable);

        Slice<PointTransactionGetResponse> contents = transactions.map(transaction ->
                PointTransactionGetResponse.builder()
                        .transactionId(transaction.getId())
                        .amount(transaction.getAmount().intValue())
                        .type(transaction.getType())
                        .balanceAfter(transaction.getBalanceAfter().intValue())
                        .createdAt(transaction.getCreatedAt())
                        .build()
        );

        return SliceResponse.fromSlice(contents);
    }
}