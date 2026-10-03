package com.peterstrele.trombonemaster.application.ports.outbound;

import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;
import com.peterstrele.trombonemaster.domain.model.valueobjects.Username;

public interface AccessTokenProvider {

    AccessToken createToken(
            UserId userId,
            Username username
    );
}