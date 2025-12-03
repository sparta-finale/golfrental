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

        PointAccount pointAccount = pointAccountRepository.findByUserId(userId).orElseGet(() -> {

            User user = userQueryService.findById(userId);

            return pointAccountRepository.save(PointAccount.createDefault(user));
        });

        return PointBalanceResponse.builder()
                .pointAccountId(pointAccount.getId())
                .balance(pointAccount.getBalance())
                .build();
    }

    @Override
    public PointUseResponse usePoints(Long userId, Integer amount) {

        User user = userQueryService.findById(userId);

        // 자동 계좌 생성
        PointAccount account = pointAccountRepository.findByUserId(userId)
                .orElseGet(() ->
                        pointAccountRepository.save(PointAccount.createDefault(user))
                );

        // 도메인 규칙 실행 (잔액 검증 + 차감)
        account.use(amount);

        // 거래내역 기록
        PointTransaction transaction = PointTransaction.create(
                user,
                amount.longValue(),
                PointTransactionType.USE,
                account.getBalance().longValue()
        );

        pointTransactionRepository.save(transaction);

        return PointUseResponse.builder()
                .pointAccountId(account.getId())
                .usedAmount(amount)
                .balanceAfterUse(account.getBalance())
                .build();
    }
}
