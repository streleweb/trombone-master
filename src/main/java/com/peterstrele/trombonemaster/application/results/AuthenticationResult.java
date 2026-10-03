package com.peterstrele.trombonemaster.application.results;

public record AuthenticationResult(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}