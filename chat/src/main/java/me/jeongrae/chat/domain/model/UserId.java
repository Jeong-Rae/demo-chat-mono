package me.jeongrae.chat.domain.model;

import lombok.EqualsAndHashCode;
import me.jeongrae.chat.common.guard.Guard;

/**
 * 시스템에 접속한 사용자를 나타내는 객체.
 * Guest 사용자이며 username으로 식별됨.
 */
@EqualsAndHashCode
public final class UserId {
    private final String value;

    private UserId(String value) {
        this.value = Guard.notBlank(value, "UserId 값은 비어있을 수 없습니다.");
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