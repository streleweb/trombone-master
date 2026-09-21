package com.peterstrele.trombonemaster.infrastructure.postgresql.entities;

import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;

public final class UserEntityMapper {

    private UserEntityMapper() {
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId().uuid(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getDisplayName(),
                user.getCountry()
        );
    }

    public static User toDomain(UserEntity entity) {
        return User.reconstitute(
                new UserId(entity.getId()),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getDisplayName(),
                entity.getCountry()
        );
    }
}