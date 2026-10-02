package com.peterstrele.trombonemaster.domain.model.valueobjects;

import java.util.Locale;

public record Username(String value) {

    public Username {
        if (value == null) {
            throw new IllegalArgumentException("Username must not be null");
        }

        value = value.trim().toLowerCase(Locale.ROOT);

        if (value.length() < 3 || value.length() > 30) {
            throw new IllegalArgumentException(
                    "Username must be between 3 and 30 characters"
            );
        }

        if (!value.matches("^[a-z][a-z0-9_-]*$")) {
            throw new IllegalArgumentException(
                    "Username has an invalid format"
            );
        }
    }

    public static Username of(String value) {
        return new Username(value);
    }
}