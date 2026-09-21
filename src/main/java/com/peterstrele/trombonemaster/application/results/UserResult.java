package com.peterstrele.trombonemaster.application.results;

import java.util.UUID;

public record UserResult(
        UUID id,
        String username,
        String email,
        String displayName,
        String country
) {}