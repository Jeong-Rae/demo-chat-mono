package me.jeongrae.chat.domain.model;

import java.time.Instant;
import java.util.Objects;

public class ChatMessage implements Identifiable<MessageId> {

    private final MessageId messageId;
    private final RoomId roomId;
    private final UserId senderId;
    private final ChatText text;
    private final Instant sentAt;

    private ChatMessage(RoomId roomId, UserId senderId, ChatText text) {
        Objects.requireNonNull(roomId, "roomId must not be null");
        Objects.requireNonNull(senderId, "senderId must not be null");
        Objects.requireNonNull(text, "text must not be null");

        this.messageId = MessageId.generate();
        this.roomId = roomId;
        this.senderId = senderId;
        this.text = text;
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