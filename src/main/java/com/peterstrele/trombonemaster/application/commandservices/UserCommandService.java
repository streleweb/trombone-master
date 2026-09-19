package com.peterstrele.trombonemaster.application.commandservices;

import com.peterstrele.trombonemaster.application.commands.CreateUserCommand;
import com.peterstrele.trombonemaster.application.exceptions.DisplayNameAlreadyTakenException;
import com.peterstrele.trombonemaster.application.exceptions.EmailAlreadyTakenException;
import com.peterstrele.trombonemaster.application.exceptions.UsernameAlreadyTakenException;
import com.peterstrele.trombonemaster.application.ports.outbound.UserRepository;
import com.peterstrele.trombonemaster.application.results.CreatedUser;
import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import org.springframework.stereotype.Service;

@Service
public class UserCommandService {
    private final UserRepository userRepository;

    public UserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CreatedUser createUser(CreateUserCommand createUserCommand) {

        /**
         * fields are being normalized/trimmed within the create method
         */
        User user = User.create(
                createUserCommand.username(),
                createUserCommand.email(),
                createUserCommand.displayName(),
                createUserCommand.country()
        );

        if (userRepository.existsByUsername(user.getUsername())){
            throw new UsernameAlreadyTakenException();
        }

        if (userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyTakenException();
        }

        if (userRepository.existsByDisplayName(user.getDisplayName())){
            throw new DisplayNameAlreadyTakenException();
        }

        User savedUser = userRepository.save(user);

        return new CreatedUser(
                savedUser.getId().uuid(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getDisplayName(),
                savedUser.getCountry()
        );

    }
}
