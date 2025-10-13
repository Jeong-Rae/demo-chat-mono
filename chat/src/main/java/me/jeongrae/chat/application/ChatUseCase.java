package me.jeongrae.chat.application;

import me.jeongrae.chat.domain.chat.model.ChatMessage;
import me.jeongrae.chat.domain.chat.model.ChatRoom;
import me.jeongrae.chat.domain.chat.model.UserId;

public interface ChatUseCase {

    void join(UserId userId, String username);

    ChatRoom getOrCreateRoom(UserId user1, UserId user2);

    ChatMessage sendMessage(SendMessageCommand command);

    void leave(UserId userId);
}
