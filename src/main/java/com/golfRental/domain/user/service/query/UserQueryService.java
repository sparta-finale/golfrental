package com.golfRental.domain.user.service.query;

import com.golfRental.domain.user.dto.response.UserGetInfoResponse;
import com.golfRental.domain.user.dto.response.UserGetMyInfoResponse;
import com.golfRental.domain.user.entity.User;

public interface UserQueryService {

    // 내 정보 조회
    UserGetMyInfoResponse getMyInfo(Long myId);

    // 유저 정보 조회
    UserGetInfoResponse getInfo(Long userId);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByPhoneNumber(String phoneNumber);

    User findByEmail(String email);

    User findById(Long userId);
}
