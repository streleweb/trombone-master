package com.peterstrele.trombonemaster.infrastructure.postgresql.repositories;

import com.peterstrele.trombonemaster.infrastructure.postgresql.entities.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Locale;

public final class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<UserEntity> usernameContainsIgnoreCase(
            String username
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")),
                        "%" + username.toLowerCase(Locale.ROOT) + "%"
                );
    }

    public static Specification<UserEntity> emailEqualsIgnoreCase(
            String email
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("email")),
                        email.toLowerCase(Locale.ROOT)
                );
    }

    public static Specification<UserEntity> displayNameContainsIgnoreCase(
            String displayName
    ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("displayName")),
                        "%" + displayName.toLowerCase(Locale.ROOT) + "%"
                );
    }

    public static Specification<UserEntity> countryIn(
            List<String> countries
    ) {
        List<String> normalizedCountries = countries.stream()
                .map(country -> country.toUpperCase(Locale.ROOT))
                .toList();

        return (root, query, criteriaBuilder) ->
                root.get("country").in(normalizedCountries);
    }
}