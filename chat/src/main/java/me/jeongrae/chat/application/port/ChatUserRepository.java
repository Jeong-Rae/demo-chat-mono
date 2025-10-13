package me.jeongrae.chat.application.port;

import me.jeongrae.chat.domain.chat.model.ChatUser;
import me.jeongrae.chat.domain.chat.model.UserId;

import java.util.Optional;

public interface ChatUserRepository {
    Optional<ChatUser> findById(UserId userId);

    void save(ChatUser chatUser);
}
