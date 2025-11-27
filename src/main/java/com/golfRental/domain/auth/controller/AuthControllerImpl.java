package com.golfRental.domain.auth.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.request.AuthLoginRequest;
import com.golfRental.domain.auth.dto.request.AuthSignupRequest;
import com.golfRental.domain.auth.dto.response.AuthLoginResponse;
import com.golfRental.domain.auth.message.AuthSuccessMessage;
import com.golfRental.domain.auth.service.command.AuthCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthControllerImpl implements AuthController {

    private final AuthCommandService authCommandService;

    @Override
    @PostMapping("/signup")
    public ResponseEntity<CommonApiResponse<Void>> signup(
            @Valid @RequestBody AuthSignupRequest authSignupRequest
    ) {
        authCommandService.signup(authSignupRequest);

        return CommonApiResponse.created(null, AuthSuccessMessage.SIGNUP);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<CommonApiResponse<AuthLoginResponse>> login(
            @Valid @RequestBody AuthLoginRequest authLoginRequest
    ) {
        AuthLoginResponse authLoginResponse = authCommandService.login(authLoginRequest);

        return CommonApiResponse.success(authLoginResponse, AuthSuccessMessage.LOGIN);
    }
}
