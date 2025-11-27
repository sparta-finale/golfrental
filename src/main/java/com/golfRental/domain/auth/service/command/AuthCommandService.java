package com.golfRental.domain.auth.service.command;

import com.golfRental.domain.auth.dto.request.AuthSignupRequest;

public interface AuthCommandService {

    void signup(AuthSignupRequest authSignupRequest);
}
