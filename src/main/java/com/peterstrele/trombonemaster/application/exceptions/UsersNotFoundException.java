package com.peterstrele.trombonemaster.application.exceptions;

public class UsersNotFoundException extends RuntimeException {
    public UsersNotFoundException() {
        super("Users not found");
    }
}
