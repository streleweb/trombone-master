package com.peterstrele.trombonemaster.infrastructure.postgresql.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    private String username;
    private String email;
    private String displayName;
    private String country;

    protected UserEntity() {
        // Required by JPA
    }

    public UserEntity(
            UUID id,
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

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCountry() {
        return country;
    }
}