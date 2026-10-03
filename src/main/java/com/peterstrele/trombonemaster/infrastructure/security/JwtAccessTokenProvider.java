package com.peterstrele.trombonemaster.infrastructure.security;

import com.peterstrele.trombonemaster.application.ports.outbound.AccessToken;
import com.peterstrele.trombonemaster.application.ports.outbound.AccessTokenProvider;
import com.peterstrele.trombonemaster.domain.model.valueobjects.UserId;
import com.peterstrele.trombonemaster.domain.model.valueobjects.Username;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
public class JwtAccessTokenProvider implements AccessTokenProvider {

    private final JwtEncoder jwtEncoder;
    private final Duration expiration;

    public JwtAccessTokenProvider(
            JwtEncoder jwtEncoder,
            @Value("${security.jwt.access-token-expiration}")
            Duration expiration
    ) {
        this.jwtEncoder = jwtEncoder;
        this.expiration = expiration;
    }

    @Override
    public AccessToken createToken(
            UserId userId,
            Username username
    ) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("trombone-master")
                .subject(userId.uuid().toString())
                .issuedAt(now)
                .expiresAt(now.plus(expiration))
                .id(UUID.randomUUID().toString())
                .claim("username", username.value())
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        String token = jwtEncoder
                .encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();

        return new AccessToken(
                token,
                expiration.toSeconds()
        );
    }
}