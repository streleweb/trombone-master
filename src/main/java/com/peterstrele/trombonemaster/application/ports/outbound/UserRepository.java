package com.peterstrele.trombonemaster.application.ports.outbound;

import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import com.peterstrele.trombonemaster.domain.model.valueobjects.DisplayName;
import com.peterstrele.trombonemaster.domain.model.valueobjects.EmailAddress;
import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;
import com.peterstrele.trombonemaster.domain.model.valueobjects.Username;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByUsername(Username username);

    boolean existsByUsername(Username username);

    boolean existsByEmail(EmailAddress email);

    boolean existsByDisplayName(DisplayName displayName);

    boolean isUsernameTakenByAnotherUser(Username username, UserId userId);

    boolean isEmailTakenByAnotherUser(EmailAddress email, UserId userId);

    boolean isDisplayNameTakenByAnotherUser(DisplayName displayName, UserId userId);

    Page<User> findUsers(
            int page,
            int size,
            String username,
            String email,
            String displayName,
            List<String> countries
    );
}
