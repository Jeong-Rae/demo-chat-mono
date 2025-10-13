package me.jeongrae.chat.domain.authn.member;

import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;

/**
 * Guest 사용자를 식별하는 값 객체.
 */
public record GuestId(String value) {

    public GuestId {
        Guard.notBlank(value, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("guestId"));
    }

    public static GuestId of(String value) {
        return new GuestId(value);
    }
}
