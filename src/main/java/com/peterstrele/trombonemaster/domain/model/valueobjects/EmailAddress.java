package com.peterstrele.trombonemaster.domain.model.valueobjects;

import java.util.Locale;

public record EmailAddress(String value) {

    public EmailAddress {
        if (value == null) {
            throw new IllegalArgumentException("Email must not be null");
        }

        value = value.trim().toLowerCase(Locale.ROOT);

        if (value.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }

        if (value.length() > 254) {
            throw new IllegalArgumentException(
                    "Email must not exceed 254 characters"
            );
        }
    }

    public static EmailAddress of(String value) {
        return new EmailAddress(value);
    }
}