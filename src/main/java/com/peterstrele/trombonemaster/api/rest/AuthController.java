package com.peterstrele.trombonemaster.api.rest;

import com.peterstrele.trombonemaster.api.rest.transform.UserCommandAssembler;
import com.peterstrele.trombonemaster.application.commandservices.AuthCommandService;
import com.peterstrele.trombonemaster.application.results.UserResult;
import com.peterstrele.trombonemaster.generated.api.AuthApi;
import com.peterstrele.trombonemaster.generated.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {

    private final AuthCommandService authCommandService;

    public AuthController(AuthCommandService authCommandService) {
        this.authCommandService = authCommandService;
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Void> logout(RefreshTokenRequest refreshTokenRequest) {
        return null;
    }

    @Override
    public ResponseEntity<TokenResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return null;
    }

    @Override
    public ResponseEntity<UserResponse> registerUser(
            RegisterRequest registerRequest
    ) {
        UserResult userResult = authCommandService.registerUser(
                UserCommandAssembler.toRegisterUserCommand(registerRequest)
        );

        UserResponse response =
                UserCommandAssembler.toUserResponse(userResult);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}