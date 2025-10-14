package me.jeongrae.chat.domain.authn.member;

import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;

/**
 * Member 사용자를 식별하는 값 객체.
 */
public record MemberId(String value) {

    public MemberId {
        Guard.notBlank(value, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("memberId"));
    }

    public static MemberId of(String value) {
        return new MemberId(value);
    }
}
