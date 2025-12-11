package com.golfRental.domain.point.service.command;

import com.golfRental.domain.point.dto.response.PointBalanceResponse;
import com.golfRental.domain.point.dto.response.PointUseResponse;
import com.golfRental.domain.point.entity.PointAccount;
import com.golfRental.domain.point.entity.PointTransaction;
import com.golfRental.domain.point.enums.PointTransactionType;
import com.golfRental.domain.point.repository.PointAccountRepository;
import com.golfRental.domain.point.repository.PointTransactionRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PointCommandServiceImpl implements PointCommandService {

    private final PointAccountRepository pointAccountRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final UserQueryService userQueryService;

    @Override
    public PointBalanceResponse getBalance(Long userId) {
        PointAccount account = getOrCreateAccount(userId);
        return PointBalanceResponse.from(account);
    }

    @Override
    public PointUseResponse usePoints(Long userId, Long amount) {

        User user = userQueryService.findById(userId);

        // 자동 계좌 생성
        PointAccount account = getOrCreateAccount(userId);

        // 도메인 규칙 실행 (잔액 검증 + 차감)
        account.use(amount);

        // 거래내역 기록
        PointTransaction transaction = PointTransaction.create(
                user,
                amount,
                PointTransactionType.USE,
                account.getBalance()
        );

        pointTransactionRepository.save(transaction);

        return PointUseResponse.from(account, amount);
    }

    /**
     * 포인트 계좌를 조회하거나 없으면 생성
     * 동시성 문제로 인한 unique 제약 위반 시 재조회를 시도
     */
    private PointAccount getOrCreateAccount(Long userId) {
        return pointAccountRepository.findByUserId(userId)
                .orElseGet(() -> {
                    try {
                        User user = userQueryService.findById(userId);
                        return pointAccountRepository.save(PointAccount.createDefault(user));
                    } catch (DataIntegrityViolationException e) {
                        log.warn("Concurrent account creation detected for userId: {}", userId);
                        return pointAccountRepository.findByUserId(userId)
                                .orElseThrow(() -> new IllegalStateException("계좌 생성 실패"));
                    }
                });
    }
}
