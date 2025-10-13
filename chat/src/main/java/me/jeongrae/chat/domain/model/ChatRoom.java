package me.jeongrae.chat.domain.model;

import me.jeongrae.chat.domain.shared.error.ChatErrorCode;
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
        if (user1.equals(user2)) {
            throw ChatErrorCode.INVALID_PARTICIPANTS.ex("user1", user1, "user2", user2);
        }

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
        if (!this.id().equals(message.roomId())) {
            throw ChatErrorCode.MESSAGE_ROOM_MISMATCH.ex("chatRoomId", this.id(), "messageRoomId",
                    message.roomId());
        }
        if (!canSend(message.senderId())) {
            throw ChatErrorCode.SENDER_NOT_PARTICIPANT.ex("senderId", message.senderId());
        }
    }
}
