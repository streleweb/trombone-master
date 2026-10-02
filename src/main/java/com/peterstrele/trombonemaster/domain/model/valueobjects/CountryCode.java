package com.peterstrele.trombonemaster.domain.model.valueobjects;

import java.util.Locale;

public record CountryCode(String value) {

    public CountryCode {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Country code must not be null"
            );
        }

        value = value.trim().toUpperCase(Locale.ROOT);

        if (!value.matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException(
                    "Country code must contain exactly two letters"
            );
        }
    }

    public static CountryCode of(String value) {
        return new CountryCode(value);
    }
}