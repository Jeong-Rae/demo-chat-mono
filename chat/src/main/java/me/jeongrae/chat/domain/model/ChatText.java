package me.jeongrae.chat.domain.model;

import lombok.EqualsAndHashCode;

/**
 * 채팅 메시지 본문.
 * 빈 문자열 또는 null이 아니어야 하며, 길이는 4000자를 넘을 수 없음.
 */
@EqualsAndHashCode
public final class ChatText {
    private static final int MAX_LENGTH = 4000;

    private final String value;

    private ChatText(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("value must be less than or equal to " + MAX_LENGTH);
        }
        this.value = value;
    }

    public static ChatText of(String value) {
        return new ChatText(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}