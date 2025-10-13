package me.jeongrae.chat.domain.chat.model;

import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;

import java.util.UUID;

/**
 * 개별 메시지의 유니크 ID.
 * UUID 기반으로 생성됨.
 */
public record MessageId(String value) {
    public MessageId {
        Guard.notBlank(value, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("MessageId"));
    }

    public static MessageId of(String value) {
        return new MessageId(value);
    }

    public static MessageId generate() {
        return new MessageId(UUID.randomUUID().toString());
    }
}
