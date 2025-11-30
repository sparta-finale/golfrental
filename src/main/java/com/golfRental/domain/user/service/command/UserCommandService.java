package com.golfRental.domain.user.service.command;

import com.golfRental.domain.user.dto.request.UserUpdateMyInfoRequest;
import com.golfRental.domain.user.dto.request.UserUpdatePasswordRequest;
import com.golfRental.domain.user.dto.response.UserUpdateMyInfoResponse;
import com.golfRental.domain.user.entity.User;

public interface UserCommandService {

    // 내 정보 수정
    UserUpdateMyInfoResponse updateMyInfo(Long userId, UserUpdateMyInfoRequest userUpdateMyInfoRequest);

    void save(User user);

    User findById(Long userId);

    void updatePassword(Long userId, UserUpdatePasswordRequest userUpdatePasswordRequest);
}
