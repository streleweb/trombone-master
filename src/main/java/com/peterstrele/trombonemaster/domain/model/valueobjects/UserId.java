package com.peterstrele.trombonemaster.domain.model.valueobjects;

import java.util.UUID;

public record UserId(UUID uuid) {

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}
