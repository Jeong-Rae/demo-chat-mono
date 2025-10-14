package me.jeongrae.chat.domain.authn.policy;

import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.credential.Password;

/**
 * 비밀번호 해싱 및 검증을 정의한다.
 */
public interface PasswordHasher {

    HashedPassword hash(Password password);

    boolean matches(Password password, HashedPassword hashedPassword);
}