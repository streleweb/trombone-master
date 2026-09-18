package com.peterstrele.trombonemaster.application.commands;

public record CreateUserCommand(
        String username,
        String email,
        String displayName,
        String country
){}
