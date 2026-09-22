package com.peterstrele.trombonemaster.application.ports.outbound;

import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;

public interface AccessTokenProvider {

    String createToken(
            UserId userId,
            String username
    );
}