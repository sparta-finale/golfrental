package com.golfRental.domain.user.service.query;

import com.golfRental.common.response.PageResponse;
import com.golfRental.domain.user.dto.response.UserGetAllResponse;
import com.golfRental.domain.user.dto.response.UserGetInfoResponse;
import com.golfRental.domain.user.dto.response.UserGetMyInfoResponse;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.exception.UserErrorCode;
import com.golfRental.domain.user.exception.UserException;
import com.golfRental.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public UserGetMyInfoResponse getMyInfo(Long myId) {
        User user = findById(myId);

        return UserGetMyInfoResponse.from(
                user.getEmail(), user.getUsername(), user.getPhoneNumber(), user.getAddress(), user.getNickname()
        );
    }

    @Override
    public UserGetInfoResponse getInfo(Long userId) {
        User user = findById(userId);

        return UserGetInfoResponse.from(
                user.getEmail(), user.getUsername(), user.getPhoneNumber(), user.getAddress(), user.getNickname()
        );
    }

    @Override
    public PageResponse<UserGetAllResponse> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAllByDeletedAtIsNull(pageable);

        Page<UserGetAllResponse> contents = users.map(user -> UserGetAllResponse.from(
                user.getEmail(), user.getUsername(), user.getPhoneNumber(), user.getAddress(), user.getNickname()
        ));

        return PageResponse.fromPage(contents);
    }

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

    @Override
    public User findById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId).orElseThrow(
                () -> new UserException(UserErrorCode.USER_INVALID_ID)
        );
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAllUsers();
    }
}
