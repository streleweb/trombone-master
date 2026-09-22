package com.peterstrele.trombonemaster.application.commandservices;

import com.peterstrele.trombonemaster.application.commands.LoginUserCommand;
import com.peterstrele.trombonemaster.application.commands.RegisterUserCommand;
import com.peterstrele.trombonemaster.application.exceptions.DisplayNameAlreadyTakenException;
import com.peterstrele.trombonemaster.application.exceptions.EmailAlreadyTakenException;
import com.peterstrele.trombonemaster.application.exceptions.InvalidCredentialsException;
import com.peterstrele.trombonemaster.application.exceptions.UsernameAlreadyTakenException;
import com.peterstrele.trombonemaster.application.ports.outbound.AccessTokenProvider;
import com.peterstrele.trombonemaster.application.ports.outbound.PasswordHasher;
import com.peterstrele.trombonemaster.application.ports.outbound.UserRepository;
import com.peterstrele.trombonemaster.application.results.AuthenticationResult;
import com.peterstrele.trombonemaster.application.results.UserResult;
import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthCommandService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final AccessTokenProvider  accessTokenProvider;

    public AuthCommandService(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            AccessTokenProvider accessTokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.accessTokenProvider = accessTokenProvider;
    }

    public UserResult registerUser(RegisterUserCommand command) {

        // normalize and check if exists so the password doesn`t have to be hashed for nothing
        String username = normalizeUsername(command.username());
        String email = normalizeEmail(command.email());
        String displayName = normalizeDisplayName(command.displayName());
        String country = normalizeCountry(command.country());

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyTakenException();
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyTakenException();
        }

        if (userRepository.existsByDisplayName(displayName)) {
            throw new DisplayNameAlreadyTakenException();
        }

        String passwordHash = passwordHasher.hash(command.password());

        User user = User.register(
                username,
                email,
                passwordHash,
                displayName,
                country
        );

        User savedUser = userRepository.save(user);

        return new UserResult(
                savedUser.getId().uuid(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getDisplayName(),
                savedUser.getCountry()
        );
    }

    public AuthenticationResult login(LoginUserCommand command) {

        String normalizedUsername = command.username()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository
                .findByUsername(normalizedUsername)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(
                command.password(),
                user.getPasswordHash()
        )) {
            throw new InvalidCredentialsException();
        }

        String accessToken = accessTokenProvider.createToken(
                user.getId(),
                user.getUsername()
        );

        return null; //TODO
    }

    private static String normalizeUsername(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static String normalizeDisplayName(String displayName) {
        return displayName.trim().replaceAll("\\s+", " ");
    }

    private static String normalizeCountry(String country) {
        return country.trim().toUpperCase(Locale.ROOT);
    }
}