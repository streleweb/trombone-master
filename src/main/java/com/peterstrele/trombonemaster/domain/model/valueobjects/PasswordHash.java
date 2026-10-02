package com.peterstrele.trombonemaster.domain.model.valueobjects;

public record PasswordHash(String value) {

    public PasswordHash {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Password hash must not be blank"
            );
        }
    }

    public static PasswordHash of(String value) {
        return new PasswordHash(value);
    }
}