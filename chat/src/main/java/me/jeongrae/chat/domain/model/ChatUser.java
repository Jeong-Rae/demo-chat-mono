package me.jeongrae.chat.domain.model;

import me.jeongrae.chat.common.guard.Guard;
import java.time.Instant;

public class ChatUser implements Identifiable<UserId> {

    private final UserId userId;
    private final String username;
    private final Instant joinedAt;

    private ChatUser(UserId userId, String username) {
        this.userId = Guard.notNull(userId, "userId는 null일 수 없습니다.");
        this.username = Guard.notBlank(username, "username은 비어있을 수 없습니다.");
        this.joinedAt = Instant.now();
    }

    public static ChatUser of(UserId userId, String username) {
        return new ChatUser(userId, username);
    }

    @Override
    public UserId id() {
        return userId;
    }

    public String username() {
        return username;
    }

    public Instant joinedAt() {
        return joinedAt;
    }

    public boolean is(UserId userId) {
        return this.userId.equals(userId);
    }
}