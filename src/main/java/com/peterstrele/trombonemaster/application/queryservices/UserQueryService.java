package com.peterstrele.trombonemaster.application.queryservices;

import com.peterstrele.trombonemaster.application.exceptions.UserNotFoundException;
import com.peterstrele.trombonemaster.application.ports.outbound.UserRepository;
import com.peterstrele.trombonemaster.application.results.RetrievedUser;
import com.peterstrele.trombonemaster.domain.model.aggregates.User;
import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;
import com.peterstrele.trombonemaster.generated.model.UserResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserQueryService {
    private UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RetrievedUser retrieveUser(UUID id){

        User user = userRepository.findById(new UserId(id))
                .orElseThrow(UserNotFoundException::new);

        return new RetrievedUser(
                user.getId().uuid(),
                user.getUsername(),
                user.getEmail(),
                user.getDisplayName(),
                user.getCountry()
        );
    }
}
