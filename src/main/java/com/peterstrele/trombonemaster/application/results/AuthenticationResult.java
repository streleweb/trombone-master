package com.peterstrele.trombonemaster.application.results;

public record AuthenticationResult(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn
) {
}
