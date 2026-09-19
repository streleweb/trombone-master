package com.peterstrele.trombonemaster.application.ports.outbound;

import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByDisplayName(String displayName);
}
