package com.peterstrele.trombonemaster.domain.model.aggregates;

import com.peterstrele.trombonemaster.domain.model.valueobjects.*;
import lombok.Getter;

@Getter
public class User {

    private final UserId id;
    private final Username username;
    private final EmailAddress email;
    private final PasswordHash passwordHash;
    private final DisplayName displayName;
    private final CountryCode country;

    private User(
            UserId id,
            Username username,
            EmailAddress email,
            PasswordHash passwordHash,
            DisplayName displayName,
            CountryCode country
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.country = country;
    }

    public static User register(
            Username username,
            EmailAddress email,
            PasswordHash passwordHash,
            DisplayName displayName,
            CountryCode country
    ) {
        return new User(
                UserId.generate(),
                username,
                email,
                passwordHash,
                displayName,
                country
        );
    }

    public static User reconstitute(
            UserId id,
            Username username,
            EmailAddress email,
            PasswordHash passwordHash,
            DisplayName displayName,
            CountryCode country
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

    public User update(
            Username username,
            EmailAddress email,
            DisplayName displayName,
            CountryCode country
    ) {
        return new User(
                this.id,
                username,
                email,
                this.passwordHash,
                displayName,
                country
        );
    }
}