package com.golfRental.domain.user.service.query;

import com.golfRental.domain.user.entity.User;

public interface UserQueryService {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByPhoneNumber(String phoneNumber);

    User findByEmail(String email);
}
