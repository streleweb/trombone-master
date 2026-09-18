package com.peterstrele.trombonemaster.api.rest.transform;

import com.peterstrele.trombonemaster.application.commands.CreateUserCommand;
import com.peterstrele.trombonemaster.application.results.CreatedUser;
import com.peterstrele.trombonemaster.generated.model.CreateUserRequest;
import com.peterstrele.trombonemaster.generated.model.UserResponse;
import com.peterstrele.trombonemaster.generated.model.UserStatus;

public final class UserAssembler {

    private UserAssembler() {
    }

    public static CreateUserCommand toCreateUserCommand(CreateUserRequest createUserRequest) {

        return new CreateUserCommand(
                createUserRequest.getUsername(),
                createUserRequest.getEmail(),
                createUserRequest.getDisplayName(),
                createUserRequest.getCountry());
    }

    public static UserResponse toUserResponse(CreatedUser createdUser) {
        return new UserResponse(
                createdUser.id(),
                createdUser.username(),
                createdUser.email(),
                createdUser.displayName(),
                createdUser.country()
                );
    }
}
