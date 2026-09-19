package com.peterstrele.trombonemaster.api.rest.transform;

import com.peterstrele.trombonemaster.application.results.RetrievedUser;
import com.peterstrele.trombonemaster.generated.model.UserResponse;

public final class UserQueryAssembler {

    private UserQueryAssembler() {
    }

    public static UserResponse toUserResponse(RetrievedUser retrievedUser) {
        return new UserResponse(
                retrievedUser.id(),
                retrievedUser.username(),
                retrievedUser.email(),
                retrievedUser.displayName(),
                retrievedUser.country()
        );
    }
}
