package me.jeongrae.chat.domain.model;

import lombok.EqualsAndHashCode;
import me.jeongrae.chat.common.guard.Guard;

import java.util.UUID;

/**
 * 개별 메시지의 유니크 ID.
 * UUID 기반으로 생성됨.
 */
@EqualsAndHashCode
public final class MessageId {
    private final String value;

    private MessageId(String value) {
        this.value = Guard.notBlank(value, "MessageId 값은 비어있을 수 없습니다.");
    }

    public static MessageId of(String value) {
        return new MessageId(value);
    }

    public static MessageId generate() {
        return new MessageId(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}