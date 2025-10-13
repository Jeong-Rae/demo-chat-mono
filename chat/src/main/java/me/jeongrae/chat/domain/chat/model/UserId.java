package me.jeongrae.chat.domain.chat.model;

import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;

/**
 * 시스템에 접속한 사용자를 나타내는 객체.
 * Guest 사용자이며 username으로 식별됨.
 */
public record UserId(String value) {
    public UserId {
        Guard.notBlank(value, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("UserId"));
    }

    public static UserId of(String value) {
        return new UserId(value);
    }
}
