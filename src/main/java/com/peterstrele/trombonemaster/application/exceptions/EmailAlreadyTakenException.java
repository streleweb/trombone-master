package com.peterstrele.trombonemaster.application.exceptions;

public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException(){

        super("Email is already taken.");
    }
}
