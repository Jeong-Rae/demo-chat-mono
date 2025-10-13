package me.jeongrae.chat.application;

import lombok.Getter;
import me.jeongrae.chat.domain.chat.model.ChatText;
import me.jeongrae.chat.domain.chat.model.RoomId;
import me.jeongrae.chat.domain.chat.model.UserId;
import org.springframework.util.Assert;

@Getter
public class SendMessageCommand {

    private final RoomId roomId;
    private final UserId senderId;
    private final ChatText content;

    public SendMessageCommand(RoomId roomId, UserId senderId, ChatText content) {
        Assert.notNull(roomId, "roomId must not be null");
        Assert.notNull(senderId, "senderId must not be null");
        Assert.notNull(content, "content must not be null");
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
    }
}
