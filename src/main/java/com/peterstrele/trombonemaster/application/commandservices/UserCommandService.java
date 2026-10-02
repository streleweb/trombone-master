package com.peterstrele.trombonemaster.application.commandservices;

import com.peterstrele.trombonemaster.application.commands.UpdateUserCommand;
import com.peterstrele.trombonemaster.application.exceptions.DisplayNameAlreadyTakenException;
import com.peterstrele.trombonemaster.application.exceptions.EmailAlreadyTakenException;
import com.peterstrele.trombonemaster.application.exceptions.UserNotFoundException;
import com.peterstrele.trombonemaster.application.exceptions.UsernameAlreadyTakenException;
import com.peterstrele.trombonemaster.application.ports.outbound.UserRepository;
import com.peterstrele.trombonemaster.application.results.UserResult;
import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import com.peterstrele.trombonemaster.domain.model.valueobjects.CountryCode;
import com.peterstrele.trombonemaster.domain.model.valueobjects.DisplayName;
import com.peterstrele.trombonemaster.domain.model.valueobjects.EmailAddress;
import com.peterstrele.trombonemaster.domain.model.valueobjects.Username;
import org.springframework.stereotype.Service;

@Service
public class UserCommandService {

    private final UserRepository userRepository;

    public UserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResult updateUser(UpdateUserCommand command) {

        User existingUser = userRepository
                .findById(command.id())
                .orElseThrow(UserNotFoundException::new);

        Username username = Username.of(command.username());
        EmailAddress email = EmailAddress.of(command.email());
        DisplayName displayName = DisplayName.of(command.displayName());
        CountryCode country = CountryCode.of(command.country());

        User updatedUser = existingUser.update(
                username,
                email,
                displayName,
                country
        );

        if (userRepository.isUsernameTakenByAnotherUser(
                updatedUser.getUsername(),
                updatedUser.getId()
        )) {
            throw new UsernameAlreadyTakenException();
        }

        if (userRepository.isEmailTakenByAnotherUser(
                updatedUser.getEmail(),
                updatedUser.getId()
        )) {
            throw new EmailAlreadyTakenException();
        }

        if (userRepository.isDisplayNameTakenByAnotherUser(
                updatedUser.getDisplayName(),
                updatedUser.getId()
        )) {
            throw new DisplayNameAlreadyTakenException();
        }

        User savedUser = userRepository.save(updatedUser);

        return toUserResult(savedUser);
    }

    private UserResult toUserResult(User user) {
        return new UserResult(
                user.getId().uuid(),
                user.getUsername().value(),
                user.getEmail().value(),
                user.getDisplayName().value(),
                user.getCountry().value()
        );
    }
}