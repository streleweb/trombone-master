package com.peterstrele.trombonemaster.infrastructure.repositories;

import com.peterstrele.trombonemaster.application.ports.outbound.UserRepository;
import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;
import com.peterstrele.trombonemaster.infrastructure.postgresql.entities.UserEntity;
import com.peterstrele.trombonemaster.infrastructure.postgresql.entities.UserEntityMapper;
import com.peterstrele.trombonemaster.infrastructure.postgresql.repositories.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

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
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByDisplayName(String displayName) {
        return repository.existsByDisplayName(displayName);
    }
}