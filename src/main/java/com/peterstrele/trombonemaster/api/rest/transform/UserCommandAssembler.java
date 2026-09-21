package com.peterstrele.trombonemaster.api.rest.transform;

import com.peterstrele.trombonemaster.application.commands.RegisterUserCommand;
import com.peterstrele.trombonemaster.application.commands.UpdateUserCommand;
import com.peterstrele.trombonemaster.application.results.UserResult;
import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;
import com.peterstrele.trombonemaster.generated.model.RegisterRequest;
import com.peterstrele.trombonemaster.generated.model.UpdateUserRequest;
import com.peterstrele.trombonemaster.generated.model.UserResponse;

import java.util.UUID;

public final class UserCommandAssembler {

    private UserCommandAssembler() {
    }

    public static RegisterUserCommand toRegisterUserCommand(
            RegisterRequest request
    ) {
        return new RegisterUserCommand(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getDisplayName(),
                request.getCountry()
        );
    }

    public static UpdateUserCommand toUpdateUserCommand(UUID userId, UpdateUserRequest updateUserRequest) {

        return new UpdateUserCommand(
                new UserId(userId),
                updateUserRequest.getUsername(),
                updateUserRequest.getEmail(),
                updateUserRequest.getDisplayName(),
                updateUserRequest.getCountry()
        );
    }

    public static UserResponse toUserResponse(UserResult userResult) {

        return new UserResponse(
                userResult.id(),
                userResult.username(),
                userResult.email(),
                userResult.displayName(),
                userResult.country()
                );
    }
}