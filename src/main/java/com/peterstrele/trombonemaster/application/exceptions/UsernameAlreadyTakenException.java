package com.peterstrele.trombonemaster.application.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException{

    public UsernameAlreadyTakenException(){
        super("Username is already taken");
    }
}
