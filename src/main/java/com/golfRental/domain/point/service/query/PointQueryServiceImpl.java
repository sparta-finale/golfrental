package com.golfRental.domain.point.service.query;

import com.golfRental.domain.point.dto.response.PointBalanceResponse;
import com.golfRental.domain.point.entity.PointAccount;
import com.golfRental.domain.point.repository.PointAccountRepository;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointQueryServiceImpl implements PointQueryService {

    private final PointAccountRepository pointAccountRepository;
    private final UserQueryService userQueryService;

    @Override
    public PointBalanceResponse getBalance(Long userId) {

        userQueryService.findById(userId);

        PointAccount account = pointAccountRepository.findByUserId(userId)
                .orElseGet(() -> PointAccount.createDefault(userQueryService.findById(userId)));

        return PointBalanceResponse.builder()
                .pointAccountId(account.getId())
                .balance(account.getBalance())
                .build();
    }
}
