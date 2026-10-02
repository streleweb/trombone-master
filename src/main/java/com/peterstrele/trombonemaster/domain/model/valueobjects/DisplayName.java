package com.peterstrele.trombonemaster.domain.model.valueobjects;

public record DisplayName(String value) {

    public DisplayName {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Display name must not be null"
            );
        }

        value = value.trim().replaceAll("\\s+", " ");

        if (value.length() < 2 || value.length() > 30) {
            throw new IllegalArgumentException(
                    "Display name must be between 2 and 30 characters"
            );
        }
    }

    public static DisplayName of(String value) {
        return new DisplayName(value);
    }
}