package me.jeongrae.chat.infrastructure.security;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.authn.policy.PasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Argon2PasswordHasher implements PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    @Override
    public HashedPassword hash(Password password) {
        String hashedPasswordValue = passwordEncoder.encode(password.value());
        return HashedPassword.of(hashedPasswordValue);
    }

    @Override
    public boolean matches(Password password, HashedPassword hashedPassword) {
        return passwordEncoder.matches(password.value(), hashedPassword.value());
    }
}
