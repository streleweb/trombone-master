package com.peterstrele.trombonemaster.infrastructure.postgresql.repositories;

import com.peterstrele.trombonemaster.infrastructure.postgresql.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository
        extends JpaRepository<UserEntity, UUID>,
                JpaSpecificationExecutor<UserEntity> {

    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByDisplayName(String displayName);

    boolean existsByUsernameAndIdNot(String username, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByDisplayNameAndIdNot(String displayName, UUID id);
}