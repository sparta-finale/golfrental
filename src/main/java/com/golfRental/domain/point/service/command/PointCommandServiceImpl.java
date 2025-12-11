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

        // User 조회 (중복 조회 제거 기반 데이터)
        User user = userQueryService.findById(userId);

        // 계좌 조회 또는 생성 (User 기반)
        PointAccount account = getOrCreateAccount(user);

        // 잔액 검증 + 차감
        account.use(amount);

        // 거래내역 생성
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
     * 기존 방식: userId 기반 (getBalance 등 외부 영향 없게 유지)
     */
    private PointAccount getOrCreateAccount(Long userId) {
        return pointAccountRepository.findByUserId(userId)
                .orElseGet(() -> createAccount(userQueryService.findById(userId)));
    }

    private PointAccount getOrCreateAccount(User user) {
        return pointAccountRepository.findByUserId(user.getId())
                .orElseGet(() -> createAccount(user));
    }

    // 계좌 생성 로직 공통 처리
    private PointAccount createAccount(User user) {
        try {
            return pointAccountRepository.save(PointAccount.createDefault(user));
        } catch (DataIntegrityViolationException e) {
            log.warn("Concurrent account creation detected for userId: {}", user.getId());

            return pointAccountRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new IllegalStateException("계좌 생성 실패"));
        }
    }
}
