package com.peterstrele.trombonemaster.application.ports.outbound;

public record AccessToken(
        String value,
        long expiresIn
) {
}