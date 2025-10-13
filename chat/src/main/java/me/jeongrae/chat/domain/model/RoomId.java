package me.jeongrae.chat.domain.model;

import lombok.EqualsAndHashCode;
import me.jeongrae.chat.common.guard.Guard;

import java.util.stream.Stream;

/**
 * 두 사용자의 username으로 구성된 방 식별자.
 * `chat:{min(usernameA, usernameB)}:{max(usernameA, usernameB)}` 형식을 가짐.
 */
@EqualsAndHashCode
public final class RoomId {
    private final String value;

    private RoomId(String value) {
        this.value = Guard.notBlank(value, "RoomId 값은 비어있을 수 없습니다.");
    }

    public static RoomId of(String value) {
        return new RoomId(value);
    }

    public static RoomId of(UserId user1, UserId user2) {
        Guard.notNull(user1, "user1은 null일 수 없습니다.");
        Guard.notNull(user2, "user2는 null일 수 없습니다.");

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