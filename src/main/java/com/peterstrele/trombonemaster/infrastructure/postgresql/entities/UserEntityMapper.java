package com.peterstrele.trombonemaster.infrastructure.postgresql.entities;

import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import com.peterstrele.trombonemaster.domain.model.valueobjects.*;

public final class UserEntityMapper {

    private UserEntityMapper() {
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId().uuid(),
                user.getUsername().value(),
                user.getEmail().value(),
                user.getPasswordHash().value(),
                user.getDisplayName().value(),
                user.getCountry().value()
        );
    }

    public static User toDomain(UserEntity entity) {
        return User.reconstitute(
                new UserId(entity.getId()),
                Username.of(entity.getUsername()),
                EmailAddress.of(entity.getEmail()),
                PasswordHash.of(entity.getPasswordHash()),
                DisplayName.of(entity.getDisplayName()),
                CountryCode.of(entity.getCountry())
        );
    }
}