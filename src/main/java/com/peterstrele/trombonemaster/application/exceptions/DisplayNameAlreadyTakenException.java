package com.peterstrele.trombonemaster.application.exceptions;

public class DisplayNameAlreadyTakenException extends RuntimeException {
    public DisplayNameAlreadyTakenException() {

        super("This Display Name is already taken");
    }
}
