package com.peterstrele.trombonemaster.domain.model.aggregates;

import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;
import lombok.Getter;

import java.util.Locale;

@Getter
public class User {

    private final UserId id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final String displayName;
    private final String country;


    private User(
            UserId id,
            String username,
            String email,
            String passwordHash,
            String displayName,
            String country
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.country = country;

    }

    /**
     * Creates a new User with a newly generated identity.
     */
    public static User register(
            String username,
            String email,
            String passwordHash,
            String displayName,
            String country

    ) {
        return new User(
                UserId.generate(),
                normalizeUsername(username),
                normalizeEmail(email),
                passwordHash,
                normalizeDisplayName(displayName),
                normalizeCountry(country)

        );
    }

    /**
     * Restores an existing User from persistence.
     *
     * The persisted state is assumed to already represent valid domain data,
     * so no new identity is generated.
     */
    public static User reconstitute(
            UserId id,
            String username,
            String email,
            String passwordHash,
            String displayName,
            String country

    ) {
        return new User(
                id,
                username,
                email,
                passwordHash,
                displayName,
                country
        );
    }

    /**
     * Returns an updated User while preserving the existing identity.
     *
     * User is immutable, therefore updating creates a new domain object
     * instead of modifying the current instance.
     */
    public User update(
            String username,
            String email,
            String displayName,
            String country
    ) {
        return new User(
                this.id,
                normalizeUsername(username),
                normalizeEmail(email),
                this.passwordHash,
                normalizeDisplayName(displayName),
                normalizeCountry(country)
        );
    }

    private static String normalizeUsername(String username) {
        return username
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private static String normalizeEmail(String email) {
        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private static String normalizeDisplayName(String displayName) {
        return displayName
                .trim()
                .replaceAll("\\s+", " ");
    }

    private static String normalizeCountry(String country) {
        return country
                .trim()
                .toUpperCase(Locale.ROOT);
    }
}