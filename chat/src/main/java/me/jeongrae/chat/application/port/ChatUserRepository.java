package me.jeongrae.chat.application.port;

import me.jeongrae.chat.domain.model.ChatUser;
import me.jeongrae.chat.domain.model.UserId;

import java.util.Optional;

public interface ChatUserRepository {
    Optional<ChatUser> findById(UserId userId);

    void save(ChatUser chatUser);
}
