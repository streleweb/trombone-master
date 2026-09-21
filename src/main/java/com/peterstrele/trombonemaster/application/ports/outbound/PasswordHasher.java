package com.peterstrele.trombonemaster.application.ports.outbound;

public interface PasswordHasher {

    String hash(String password);

    boolean matches(String rawPassword, String hashedPassword);
}
