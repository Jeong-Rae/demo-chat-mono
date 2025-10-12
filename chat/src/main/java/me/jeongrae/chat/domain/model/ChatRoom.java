package me.jeongrae.chat.domain.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChatRoom implements Identifiable<RoomId> {

    private final RoomId roomId;
    private final Set<UserId> participants;
    private final Instant createdAt;

    private ChatRoom(UserId user1, UserId user2) {
        Objects.requireNonNull(user1, "user1 must not be null");
        Objects.requireNonNull(user2, "user2 must not be null");
        if (user1.equals(user2)) {
            throw new IllegalArgumentException("participants must be different");
        }

        this.roomId = RoomId.of(user1, user2);
        this.participants = new HashSet<>(Set.of(user1, user2));
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
        Objects.requireNonNull(message, "message must not be null");
        if (!this.roomId.equals(message.roomId())) {
            throw new IllegalArgumentException("message must belong to this room");
        }
        if (!canSend(message.senderId())) {
            throw new IllegalArgumentException("sender must be a participant");
        }
    }
}