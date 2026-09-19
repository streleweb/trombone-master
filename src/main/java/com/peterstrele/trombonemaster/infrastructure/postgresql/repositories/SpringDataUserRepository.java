package com.peterstrele.trombonemaster.infrastructure.postgresql.repositories;

import com.peterstrele.trombonemaster.infrastructure.postgresql.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataUserRepository
        extends JpaRepository<UserEntity, UUID> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}