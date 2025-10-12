package me.jeongrae.chat.domain.model;

import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 두 사용자의 username으로 구성된 방 식별자.
 * `chat:{min(usernameA, usernameB)}:{max(usernameA, usernameB)}` 형식을 가짐.
 */
@EqualsAndHashCode
public final class RoomId {
    private final String value;

    private RoomId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("value must not be empty");
        }
        this.value = value;
    }

    public static RoomId of(String value) {
        return new RoomId(value);
    }

    public static RoomId of(UserId user1, UserId user2) {
        Objects.requireNonNull(user1, "user1 must not be null");
        Objects.requireNonNull(user2, "user2 must not be null");

        String sortedUsers = Stream.of(user1.value(), user2.value())
                .sorted()
                .reduce((u1, u2) -> u1 + ":" + u2)
                .orElseThrow();

        return new RoomId("chat:" + sortedUsers);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}