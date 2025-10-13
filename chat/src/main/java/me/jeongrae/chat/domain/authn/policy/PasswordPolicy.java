package me.jeongrae.chat.domain.authn.policy;

import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.credential.Password;

/**
 * 비밀번호 정책을 정의한다.
 */
public interface PasswordPolicy {

    HashedPassword hash(Password password);

    boolean matches(Password password, HashedPassword hashedPassword);
}
