package me.jeongrae.chat.domain.model;

import me.jeongrae.chat.domain.shared.error.ErrorTemplate;
import me.jeongrae.chat.common.guard.Guard;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import me.jeongrae.chat.domain.shared.model.Entity;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ChatRoom extends Entity<RoomId> {

    private final Set<UserId> participants;
    private final Instant createdAt;

    private ChatRoom(RoomId roomId, Set<UserId> participants) {
        super(roomId);
        this.participants = participants;
        this.createdAt = Instant.now();
    }

    public static ChatRoom of(UserId user1, UserId user2) {
        Guard.notNull(user1, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("user1"));
        Guard.notNull(user2, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("user2"));
        Guard.notEquals(user1, user2, "채팅방 참여자는 서로 달라야 합니다.");

        return new ChatRoom(RoomId.of(user1, user2), new HashSet<>(Set.of(user1, user2)));
    }

    public boolean hasParticipant(UserId userId) {
        return this.participants.contains(userId);
    }

    public boolean canSend(UserId sender) {
        return hasParticipant(sender);
    }

    public void addMessage(ChatMessage message) {
        Guard.notNull(message, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("message"));
        Guard.equals(this.id(), message.roomId(), "메시지는 현재 채팅방에 속해야 합니다.");
        Guard.isTrue(canSend(message.senderId()), "메시지 전송자는 채팅방 참여자여야 합니다.");
    }
}