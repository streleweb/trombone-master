package com.peterstrele.trombonemaster.infrastructure.repositories;

import com.peterstrele.trombonemaster.application.ports.outbound.Page;
import com.peterstrele.trombonemaster.application.ports.outbound.UserRepository;
import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import com.peterstrele.trombonemaster.domain.model.valueobjects.DisplayName;
import com.peterstrele.trombonemaster.domain.model.valueobjects.EmailAddress;
import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;
import com.peterstrele.trombonemaster.domain.model.valueobjects.Username;
import com.peterstrele.trombonemaster.infrastructure.postgresql.entities.UserEntity;
import com.peterstrele.trombonemaster.infrastructure.postgresql.entities.UserEntityMapper;
import com.peterstrele.trombonemaster.infrastructure.postgresql.repositories.SpringDataUserRepository;
import com.peterstrele.trombonemaster.infrastructure.postgresql.repositories.UserSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepository repository;

    public JpaUserRepository(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);
        UserEntity savedEntity = repository.save(entity);

        return UserEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return repository.findById(id.uuid())
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(Username username) {
        return repository.findByUsername(username.value())
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(Username username) {
        return repository.existsByUsername(username.value());
    }

    @Override
    public boolean existsByEmail(EmailAddress email) {
        return repository.existsByEmail(email.value());
    }

    @Override
    public boolean existsByDisplayName(DisplayName displayName) {
        return repository.existsByDisplayName(displayName.value());
    }

    @Override
    public boolean isUsernameTakenByAnotherUser(
            Username username,
            UserId userId
    ) {
        return repository.existsByUsernameAndIdNot(
                username.value(),
                userId.uuid()
        );
    }

    @Override
    public boolean isEmailTakenByAnotherUser(
            EmailAddress email,
            UserId userId
    ) {
        return repository.existsByEmailAndIdNot(
                email.value(),
                userId.uuid()
        );
    }

    @Override
    public boolean isDisplayNameTakenByAnotherUser(
            DisplayName displayName,
            UserId userId
    ) {
        return repository.existsByDisplayNameAndIdNot(
                displayName.value(),
                userId.uuid()
        );
    }

    @Override
    public Page<User> findUsers(
            int page,
            int size,
            String username,
            String email,
            String displayName,
            List<String> countries
    ) {
        Specification<UserEntity> specification = null;

        if (username != null && !username.isBlank()) {
            specification =
                    UserSpecification.usernameContainsIgnoreCase(username);
        }

        if (email != null && !email.isBlank()) {
            Specification<UserEntity> emailSpecification =
                    UserSpecification.emailEqualsIgnoreCase(email);

            specification = specification == null
                    ? emailSpecification
                    : specification.and(emailSpecification);
        }

        if (displayName != null && !displayName.isBlank()) {
            Specification<UserEntity> displayNameSpecification =
                    UserSpecification.displayNameContainsIgnoreCase(displayName);

            specification = specification == null
                    ? displayNameSpecification
                    : specification.and(displayNameSpecification);
        }

        if (countries != null && !countries.isEmpty()) {
            Specification<UserEntity> countrySpecification =
                    UserSpecification.countryIn(countries);

            specification = specification == null
                    ? countrySpecification
                    : specification.and(countrySpecification);
        }

        org.springframework.data.domain.Page<UserEntity> result =
                repository.findAll(
                        specification,
                        PageRequest.of(
                                page,
                                size,
                                Sort.by(Sort.Direction.ASC, "username")
                        )
                );

        List<User> users = result.getContent()
                .stream()
                .map(UserEntityMapper::toDomain)
                .toList();

        return new Page<>(
                users,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}