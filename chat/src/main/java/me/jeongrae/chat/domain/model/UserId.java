package me.jeongrae.chat.domain.model;

import lombok.EqualsAndHashCode;

/**
 * 시스템에 접속한 사용자를 나타내는 객체.
 * Guest 사용자이며 username으로 식별됨.
 */
@EqualsAndHashCode
public final class UserId {
    private final String value;

    private UserId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("value must not be empty");
        }
        this.value = value;
    }

    public static UserId of(String value) {
        return new UserId(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}