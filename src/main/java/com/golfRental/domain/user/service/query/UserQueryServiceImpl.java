package com.golfRental.domain.user.service.query;

import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.exception.UserErrorCode;
import com.golfRental.domain.user.exception.UserException;
import com.golfRental.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email).orElseThrow(
                () -> new UserException(UserErrorCode.USER_INVALID_EMAIL)
        );
    }
}
