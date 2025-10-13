package me.jeongrae.chat.domain.model;

import me.jeongrae.chat.common.guard.Guard;
import java.time.Instant;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;

import me.jeongrae.chat.domain.shared.model.Entity;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ChatUser extends Entity<UserId> {

    private final String username;
    private final Instant joinedAt;

    private ChatUser(UserId userId, String username) {
        super(Guard.notNull(userId, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("userId")));
        this.username = Guard.notBlank(username, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("username"));
        this.joinedAt = Instant.now();
    }

    public static ChatUser of(UserId userId, String username) {
        return new ChatUser(userId, username);
    }

    public boolean is(UserId userId) {
        return this.id.equals(userId);
    }
}