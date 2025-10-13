package me.jeongrae.chat.domain.model;

import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;

/**
 * 채팅 메시지 본문.
 * 빈 문자열 또는 null이 아니어야 하며, 길이는 4000자를 넘을 수 없음.
 */
public record ChatText(String value) {
    private static final int MAX_LENGTH = 4000;

    public ChatText(String value) {
        Guard.notBlank(value, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("메시지 내용"));
        Guard.max(value.length(), MAX_LENGTH, String.format("메시지 내용은 %d자를 넘을 수 없습니다.", MAX_LENGTH));
        this.value = value;
    }

    public static ChatText of(String value) {
        return new ChatText(value);
    }
}