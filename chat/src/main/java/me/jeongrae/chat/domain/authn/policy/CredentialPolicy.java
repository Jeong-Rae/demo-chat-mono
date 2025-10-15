package me.jeongrae.chat.domain.authn.policy;

import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.shared.error.DomainException;

/**
 * 자격 증명의 형식 및 규칙을 검증하는 정책.
 */
public interface CredentialPolicy {

    /**
     * 사용자 이름, 닉네임, 비밀번호의 유효성을 검사.
     *
     * @param username 사용자 이름
     * @param nickname 닉네임
     * @param password 비밀번호
     * @throws DomainException 유효성 검증 실패 시
     */
    void check(String username, String nickname, Password password);
}
