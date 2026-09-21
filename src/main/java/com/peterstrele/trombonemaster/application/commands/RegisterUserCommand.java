package com.peterstrele.trombonemaster.application.commands;

public record RegisterUserCommand(
        String username,
        String email,
        String password,
        String displayName,
        String country
) {
}