package com.peterstrele.trombonemaster.api.rest.transform;

import com.peterstrele.trombonemaster.application.commands.LoginUserCommand;
import com.peterstrele.trombonemaster.generated.model.LoginRequest;

public final class AuthCommandAssembler {

    public static LoginUserCommand toLoginUserCommand(
            LoginRequest request
    ) {
        return new LoginUserCommand(
                request.getUsername(),
                request.getPassword()
        );
    }
}
