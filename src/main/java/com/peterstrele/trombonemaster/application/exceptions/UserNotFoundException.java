package com.peterstrele.trombonemaster.application.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("The User does not exist.");
    }
}
