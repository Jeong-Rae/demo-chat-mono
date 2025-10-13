package me.jeongrae.chat.domain.authn.credential;

import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;

/**
 * 해시된 비밀번호를 표현하는 값 객체.
 */
public record HashedPassword(String value) {

    public HashedPassword {
        Guard.notBlank(value, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("hashedPassword"));
    }

    public static HashedPassword of(String value) {
        return new HashedPassword(value);
    }
}
