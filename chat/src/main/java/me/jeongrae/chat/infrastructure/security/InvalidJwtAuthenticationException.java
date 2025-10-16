package me.jeongrae.chat.infrastructure.security;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {

    public InvalidJwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
