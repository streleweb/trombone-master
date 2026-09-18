package com.peterstrele.trombonemaster.application.commandservices;

import com.peterstrele.trombonemaster.application.commands.CreateUserCommand;
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
        User user = User.create(
                createUserCommand.username(),
                createUserCommand.email(),
                createUserCommand.displayName(),
                createUserCommand.country()
        );

        userRepository.save(user);

        return new CreatedUser(
                user.getId().uuid(),
                user.getUsername(),
                user.getEmail(),
                user.getDisplayName(),
                user.getCountry()
        );

    }
}
