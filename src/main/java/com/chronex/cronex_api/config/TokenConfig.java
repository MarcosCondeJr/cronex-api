package com.chronex.cronex_api.config;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.chronex.cronex_api.entity.User;

@Component
public class TokenConfig {

    @Value("${spring.security.token.secret}")
    private String secretKey = "secret";

    public String generatedToken(User user) {
        return JWT.create()
                .withClaim("userId", user.getId().toString())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(24 * 30 * 30))
                .withIssuedAt(Instant.now())
                .sign(Algorithm.HMAC256(secretKey));
    }
}
