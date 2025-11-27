package com.golfRental.domain.auth.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.request.AuthSignupRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {

    ResponseEntity<CommonApiResponse<Void>> signup(
            @Valid @RequestBody AuthSignupRequest authSignupRequest
    );
}
