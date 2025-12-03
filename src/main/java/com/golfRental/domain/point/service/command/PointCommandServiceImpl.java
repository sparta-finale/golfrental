package com.golfRental.domain.point.service.command;

import com.golfRental.domain.point.dto.response.PointBalanceResponse;
import com.golfRental.domain.point.entity.PointAccount;
import com.golfRental.domain.point.repository.PointAccountRepository;
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
}
