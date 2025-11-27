package com.golfRental.domain.auth.service.query;

import com.golfRental.domain.auth.dto.request.AuthLoginRequest;
import com.golfRental.domain.auth.dto.response.AuthLoginResponse;

public interface AuthQueryService {

    // 로그인
    AuthLoginResponse login(AuthLoginRequest authLoginRequest);
}
