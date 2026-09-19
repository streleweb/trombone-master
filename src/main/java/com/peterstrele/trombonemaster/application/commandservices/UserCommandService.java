package com.peterstrele.trombonemaster.application.commandservices;

import com.peterstrele.trombonemaster.application.commands.CreateUserCommand;
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

        if (userRepository.existsByUsername(createUserCommand.username())){
            throw new UsernameAlreadyTakenException();
        }

        if (userRepository.existsByEmail(createUserCommand.email())){
            throw new EmailAlreadyTakenException();
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
