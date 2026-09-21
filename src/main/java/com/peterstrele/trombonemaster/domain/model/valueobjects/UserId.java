package com.peterstrele.trombonemaster.domain.model.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID uuid) {

    public UserId {
        Objects.requireNonNull(uuid, "UserId must not be null");
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}