package me.jeongrae.chat.domain.model;

import java.time.Instant;
import java.util.Objects;

public class ChatUser implements Identifiable<UserId> {

    private final UserId userId;
    private final String username;
    private final Instant joinedAt;

    private ChatUser(UserId userId, String username) {
        Objects.requireNonNull(userId, "userId must not be null");
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username must not be empty");
        }

        this.userId = userId;
        this.username = username;
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