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

        if (userRepository.countByEmailAndIdNot(userUpdateMyInfoRequest.getEmail(), userId) > 0) {
            throw new UserException(UserErrorCode.USER_DUPLICATE_EMAIL);
        }
        if (userRepository.countByPhoneNumberAndIdNot(userUpdateMyInfoRequest.getPhoneNumber(), userId) > 0) {
            throw new UserException(UserErrorCode.USER_DUPLICATE_PHONE_NUMBER);
        }
        if (userRepository.countByNicknameAndIdNot(userUpdateMyInfoRequest.getNickname(), userId) > 0) {
            throw new UserException(UserErrorCode.USER_DUPLICATE_NICKNAME);
        }

        user.updateMyInfo(
                userUpdateMyInfoRequest.getEmail(), userUpdateMyInfoRequest.getUsername(),
                userUpdateMyInfoRequest.getPhoneNumber(), userUpdateMyInfoRequest.getAddress(),
                userUpdateMyInfoRequest.getNickname()
        );

        return UserUpdateMyInfoResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .nickname(user.getNickname())
                .build();
    }

    @Override
    public void updatePassword(Long userId, UserUpdatePasswordRequest userUpdatePasswordRequest) {
        User user = findById(userId);

        if (!passwordEncoder.matches(userUpdatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.USER_NOT_EQUAL_PASSWORD);
        }
        if (Objects.equals(userUpdatePasswordRequest.getOldPassword(), userUpdatePasswordRequest.getNewPassword())) {
            throw new UserException(UserErrorCode.USER_EQUAL_PASSWORD);
        }

        String encodedNewPassword = passwordEncoder.encode(userUpdatePasswordRequest.getNewPassword());

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
