package com.peterstrele.trombonemaster.domain.model.aggregates;

import ch.qos.logback.classic.spi.ConfiguratorRank;
import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;
import lombok.Getter;

@Getter
public class User {
    private final UserId id;
    private final String username;
    private final String email;
    private final String displayName;
    private final String country;

    private User(
            UserId id,
            String username,
            String email,
            String displayName,
            String country
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.country = country;
    }

    public static User create(
            String username,
            String email,
            String displayName,
            String country
    ){
        return new User(
                UserId.generate(),
                username,
                email,
                displayName,
                country
        );
    }

    /**
     * Creates a User domain model from data loaded from persistence.
     *
     * <p>This method is intended for restoring an existing User with its
     * previously assigned identity and persisted state. It does not generate
     * a new UserId.</p>
     */
    public static User reconstitute(
            UserId id,
            String username,
            String email,
            String displayName,
            String country
    ) {
        return new User(
                id,
                username,
                email,
                displayName,
                country
        );
    }
}
