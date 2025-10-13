package me.jeongrae.chat.domain.model;

import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;

import java.util.stream.Stream;

/**
 * 두 사용자의 username으로 구성된 방 식별자.
 * `chat:{min(usernameA, usernameB)}:{max(usernameA, usernameB)}` 형식을 가짐.
 */
public record RoomId(String value) {
    private static final String PREFIX = "chat";
    private static final String DELIMITER = ":";
    private static final String FORMAT = PREFIX + DELIMITER + "%s" + DELIMITER + "%s";

    public RoomId {
        Guard.notBlank(value, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("RoomId"));
    }

    public static RoomId of(String value) {
        return new RoomId(value);
    }

    public static RoomId of(UserId user1, UserId user2) {
        Guard.notNull(user1, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("user1"));
        Guard.notNull(user2, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("user2"));

        String[] sorted = Stream.of(user1.value(), user2.value())
                .sorted()
                .toArray(String[]::new);

        String formatted = String.format(FORMAT, sorted[0], sorted[1]);
        return new RoomId(formatted);
    }
}
