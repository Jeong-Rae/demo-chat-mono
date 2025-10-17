package me.jeongrae.chat.domain.authn.policy;

import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.credential.Password;

/**
 * 비밀번호 해싱 및 검증.
 */
public interface PasswordHasher {

    /**
     * 비밀번호를 해싱.
     *
     * @param password 원본 비밀번호
     * @return 해싱된 비밀번호
     */
    HashedPassword hash(Password password);

    /**
     * 비밀번호 일치 여부 검증.
     *
     * @param password 원본 비밀번호
     * @param hashedPassword 해싱된 비밀번호
     * @return 일치 시 true
     */
    boolean matches(Password password, HashedPassword hashedPassword);
}
