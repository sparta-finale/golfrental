package com.golfRental.domain.user.service.command;

import com.golfRental.domain.user.dto.request.UserUpdateMyInfoRequest;
import com.golfRental.domain.user.dto.request.UserUpdatePasswordRequest;
import com.golfRental.domain.user.dto.response.UserUpdateMyInfoResponse;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.exception.UserErrorCode;
import com.golfRental.domain.user.exception.UserException;
import com.golfRental.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserUpdateMyInfoResponse updateMyInfo(Long userId, UserUpdateMyInfoRequest userUpdateMyInfoRequest) {
        User user = findById(userId);

        if (userRepository.countByEmailAndIdNot(userUpdateMyInfoRequest.email(), userId) > 0) {
            throw new UserException(UserErrorCode.USER_DUPLICATE_EMAIL);
        }
        if (userRepository.countByPhoneNumberAndIdNot(userUpdateMyInfoRequest.phoneNumber(), userId) > 0) {
            throw new UserException(UserErrorCode.USER_DUPLICATE_PHONE_NUMBER);
        }
        if (userRepository.countByNicknameAndIdNot(userUpdateMyInfoRequest.nickname(), userId) > 0) {
            throw new UserException(UserErrorCode.USER_DUPLICATE_NICKNAME);
        }

        user.updateMyInfo(
                userUpdateMyInfoRequest.email(), userUpdateMyInfoRequest.username(),
                userUpdateMyInfoRequest.phoneNumber(), userUpdateMyInfoRequest.address(),
                userUpdateMyInfoRequest.nickname()
        );

        return UserUpdateMyInfoResponse.create(
                user.getEmail(), user.getUsername(), user.getPhoneNumber(), user.getAddress(), user.getNickname()
        );
    }

    @Override
    public void updatePassword(Long userId, UserUpdatePasswordRequest userUpdatePasswordRequest) {
        User user = findById(userId);

        if (!passwordEncoder.matches(userUpdatePasswordRequest.oldPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.USER_NOT_EQUAL_PASSWORD);
        }
        if (Objects.equals(userUpdatePasswordRequest.oldPassword(), userUpdatePasswordRequest.newPassword())) {
            throw new UserException(UserErrorCode.USER_EQUAL_PASSWORD);
        }

        String encodedNewPassword = passwordEncoder.encode(userUpdatePasswordRequest.newPassword());

        user.updatePassword(encodedNewPassword);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId).orElseThrow(
                () -> new UserException(UserErrorCode.USER_INVALID_ID)
        );
    }
}
