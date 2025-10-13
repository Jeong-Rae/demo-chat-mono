package me.jeongrae.chat.domain.model;

import me.jeongrae.chat.common.guard.Guard;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class ChatRoom implements Identifiable<RoomId> {

    private final RoomId roomId;
    private final Set<UserId> participants;
    private final Instant createdAt;

    private ChatRoom(UserId user1, UserId user2) {
        UserId u1 = Guard.notNull(user1, "user1은 null일 수 없습니다.");
        UserId u2 = Guard.notNull(user2, "user2는 null일 수 없습니다.");
        Guard.isTrue(!u1.equals(u2), "채팅방 참여자는 서로 달라야 합니다.");
        this.roomId = RoomId.of(u1, u2);
        this.participants = new HashSet<>(Set.of(u1, u2));
        this.createdAt = Instant.now();
    }

    public static ChatRoom of(UserId user1, UserId user2) {
        return new ChatRoom(user1, user2);
    }

    @Override
    public RoomId id() {
        return roomId;
    }

    public Set<UserId> participants() {
        return participants;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean hasParticipant(UserId userId) {
        return this.participants.contains(userId);
    }

    public boolean canSend(UserId sender) {
        return hasParticipant(sender);
    }

    public void addMessage(ChatMessage message) {
        Guard.notNull(message, "메시지는 null일 수 없습니다.");
        Guard.isTrue(this.roomId.equals(message.roomId()), "메시지는 현재 채팅방에 속해야 합니다.");
        Guard.isTrue(canSend(message.senderId()), "메시지 전송자는 채팅방 참여자여야 합니다.");
    }
}