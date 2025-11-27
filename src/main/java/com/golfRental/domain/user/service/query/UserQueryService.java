package com.golfRental.domain.user.service.query;

public interface UserQueryService {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByPhoneNumber(String phoneNumber);
}
