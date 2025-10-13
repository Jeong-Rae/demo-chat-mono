package me.jeongrae.chat.domain.authn.credential;

import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;

/**
 * 비밀번호 원문을 표현하는 값 객체.
 */
public record Password(String value) {

    public Password {
        Guard.notBlank(value, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("password"));
    }

    public static Password of(String value) {
        return new Password(value);
    }

    public PasswordStrength strength() {
        return PasswordStrength.evaluate(this);
    }
}
