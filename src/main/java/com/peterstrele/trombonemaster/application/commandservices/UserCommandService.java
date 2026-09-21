package com.peterstrele.trombonemaster.application.commandservices;

import com.peterstrele.trombonemaster.application.commands.RegisterUserCommand;
import com.peterstrele.trombonemaster.application.commands.UpdateUserCommand;
import com.peterstrele.trombonemaster.application.exceptions.DisplayNameAlreadyTakenException;
import com.peterstrele.trombonemaster.application.exceptions.EmailAlreadyTakenException;
import com.peterstrele.trombonemaster.application.exceptions.UserNotFoundException;
import com.peterstrele.trombonemaster.application.exceptions.UsernameAlreadyTakenException;
import com.peterstrele.trombonemaster.application.ports.outbound.PasswordHasher;
import com.peterstrele.trombonemaster.application.ports.outbound.UserRepository;
import com.peterstrele.trombonemaster.application.results.UserResult;
import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import org.springframework.stereotype.Service;

@Service
public class UserCommandService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserCommandService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }


    public UserResult updateUser(UpdateUserCommand updateUserCommand) {

        User existingUser = userRepository
                .findById(updateUserCommand.id())
                .orElseThrow(UserNotFoundException::new);

        User updatedUser = existingUser.update(
                updateUserCommand.username(),
                updateUserCommand.email(),
                updateUserCommand.displayName(),
                updateUserCommand.country()
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

        return toUserResult(savedUser); //returns new UserResult
    }

    /**
     * Mapper
     * @param user
     * @return
     */
    private UserResult toUserResult(User user) {
        return new UserResult(
                user.getId().uuid(),
                user.getUsername(),
                user.getEmail(),
                user.getDisplayName(),
                user.getCountry()
        );
    }
}
