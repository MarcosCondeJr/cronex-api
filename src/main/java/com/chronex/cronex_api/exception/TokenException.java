package com.chronex.cronex_api.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenException extends AuthenticationException {
    public TokenException(String message) {
        super(message);
    }
}
