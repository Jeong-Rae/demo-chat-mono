package me.jeongrae.chat.domain.model;

import me.jeongrae.chat.domain.shared.error.ErrorTemplate;
import me.jeongrae.chat.domain.shared.model.Entity;
import me.jeongrae.chat.common.guard.Guard;
import java.time.Instant;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ChatMessage extends Entity<MessageId> {
    private final RoomId roomId;
    private final UserId senderId;
    private final ChatText text;
    private final Instant sentAt;

    private ChatMessage(RoomId roomId, UserId senderId, ChatText text) {
        super(MessageId.generate());
        this.roomId = Guard.notNull(roomId, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("roomId"));
        this.senderId = Guard.notNull(senderId, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("senderId"));
        this.text = Guard.notNull(text, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("text"));
        this.sentAt = Instant.now();
    }

    public static ChatMessage of(RoomId roomId, UserId senderId, ChatText text) {
        return new ChatMessage(roomId, senderId, text);
    }
}