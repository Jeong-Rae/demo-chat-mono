package me.jeongrae.chat.domain.model;

import me.jeongrae.chat.common.guard.Guard;
import java.time.Instant;

import me.jeongrae.chat.domain.shared.model.Entity;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ChatUser extends Entity<UserId> {

    private final String username;
    private final Instant joinedAt;

    private ChatUser(UserId userId, String username) {
        super(Guard.notNull(userId, "userId는 null일 수 없습니다."));
        this.username = Guard.notBlank(username, "username은 비어있을 수 없습니다.");
        this.joinedAt = Instant.now();
    }

    public static ChatUser of(UserId userId, String username) {
        return new ChatUser(userId, username);
    }

    public boolean is(UserId userId) {
        return this.id.equals(userId);
    }
}