package me.jeongrae.chat.domain.model;

import me.jeongrae.chat.common.guard.Guard;
import java.time.Instant;

public class ChatMessage implements Identifiable<MessageId> {

    private final MessageId messageId;
    private final RoomId roomId;
    private final UserId senderId;
    private final ChatText text;
    private final Instant sentAt;

    private ChatMessage(RoomId roomId, UserId senderId, ChatText text) {
        this.messageId = MessageId.generate();
        this.roomId = Guard.notNull(roomId, "roomId는 null일 수 없습니다.");
        this.senderId = Guard.notNull(senderId, "senderId는 null일 수 없습니다.");
        this.text = Guard.notNull(text, "text는 null일 수 없습니다.");
        this.sentAt = Instant.now();
    }

    public static ChatMessage of(RoomId roomId, UserId senderId, ChatText text) {
        return new ChatMessage(roomId, senderId, text);
    }

    @Override
    public MessageId id() {
        return messageId;
    }

    public RoomId roomId() {
        return roomId;
    }

    public UserId senderId() {
        return senderId;
    }

    public ChatText text() {
        return text;
    }

    public Instant sentAt() {
        return sentAt;
    }
}