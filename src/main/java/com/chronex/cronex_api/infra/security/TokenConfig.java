package com.chronex.cronex_api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.chronex.cronex_api.entity.User;
import com.chronex.cronex_api.exception.TokenException;

@Component
public class TokenConfig {

    @Value("${spring.security.token.secret}")
    private String secretKey = "secret";

    public String generatedToken(User user) {
        try {

            return JWT.create()
                    .withIssuer("cronex-api")
                    .withClaim("userId", user.getId().toString())
                    .withSubject(user.getEmail())
                    .withExpiresAt(getExpirationDate())
                    .withIssuedAt(Instant.now())
                    .sign(Algorithm.HMAC256(secretKey));

        } catch (IllegalArgumentException ex) {
            throw new TokenException("Configuração inválida do token (secret).");
        } catch (JWTCreationException ex) {
            throw new TokenException("Erro ao gerar o token.");
        }
    }

    public String validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new TokenException("Token ausente.");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer("cronex-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException ex) {
            throw new TokenException("Token expirado.");
        } catch (JWTVerificationException ex) {
            throw new TokenException("Token inválido.");
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime
                .now()
                .plusHours(8)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
