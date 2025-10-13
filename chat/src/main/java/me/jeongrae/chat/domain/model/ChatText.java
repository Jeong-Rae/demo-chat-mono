package me.jeongrae.chat.domain.model;

import lombok.EqualsAndHashCode;
import me.jeongrae.chat.common.guard.Guard;

/**
 * 채팅 메시지 본문.
 * 빈 문자열 또는 null이 아니어야 하며, 길이는 4000자를 넘을 수 없음.
 */
@EqualsAndHashCode
public final class ChatText {
    private static final int MAX_LENGTH = 4000;

    private final String value;

    private ChatText(String value) {
        String text = Guard.notBlank(value, "메시지 내용은 비어있을 수 없습니다.");
        Guard.max(text.length(), MAX_LENGTH, "메시지 내용은 " + MAX_LENGTH + "자를 초과할 수 없습니다.");
        this.value = text;
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