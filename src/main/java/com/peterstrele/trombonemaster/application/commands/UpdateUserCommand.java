package com.peterstrele.trombonemaster.application.commands;

import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;

import java.util.UUID;

public record UpdateUserCommand(
        UserId id,
        String username,
        String email,
        String displayName,
        String country
) {
}