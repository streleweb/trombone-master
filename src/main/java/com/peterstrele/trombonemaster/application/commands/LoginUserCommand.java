package com.peterstrele.trombonemaster.application.commands;

public record LoginUserCommand(
        String username,
        String password
) {
}