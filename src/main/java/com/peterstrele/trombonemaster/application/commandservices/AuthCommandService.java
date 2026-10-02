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
import com.peterstrele.trombonemaster.domain.model.valueobjects.*;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthCommandService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    //private final AccessTokenProvider  accessTokenProvider;

    public AuthCommandService(
            UserRepository userRepository,
            PasswordHasher passwordHasher
    ) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;

    }

    public UserResult registerUser(RegisterUserCommand command) {

        Username username = Username.of(command.username());
        EmailAddress emailAddress = EmailAddress.of(command.email());
        DisplayName displayName = DisplayName.of(command.displayName());
        CountryCode country = CountryCode.of(command.country());

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyTakenException();
        }

        if (userRepository.existsByEmail(emailAddress)) {
            throw new EmailAlreadyTakenException();
        }

        if (userRepository.existsByDisplayName(displayName)) {
            throw new DisplayNameAlreadyTakenException();
        }

        PasswordHash passwordHash = PasswordHash.of(
                passwordHasher.hash(command.password())
        );

        User user = User.register(
                username,
                emailAddress,
                passwordHash,
                displayName,
                country
        );

        User savedUser = userRepository.save(user);

        return new UserResult(
                savedUser.getId().uuid(),
                savedUser.getUsername().value(),
                savedUser.getEmail().value(),
                savedUser.getDisplayName().value(),
                savedUser.getCountry().value()
        );
    }

    /*
    public AuthenticationResult login(LoginUserCommand command) {

       Username username = Username.of(command.username());

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(
                command.password(),
                user.getPasswordHash().value()
        )) {
            throw new InvalidCredentialsException();
        }

        String accessToken = accessTokenProvider.createToken(
                user.getId(),
                user.getUsername()
        );

        return null; //TODO
    }*/

}