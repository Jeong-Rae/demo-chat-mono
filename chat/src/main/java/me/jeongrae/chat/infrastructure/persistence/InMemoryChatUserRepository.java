package me.jeongrae.chat.infrastructure.persistence;

import me.jeongrae.chat.application.port.ChatUserRepository;
import me.jeongrae.chat.domain.chat.model.ChatUser;
import me.jeongrae.chat.domain.chat.model.UserId;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryChatUserRepository implements ChatUserRepository {

    private final Map<UserId, ChatUser> store = new ConcurrentHashMap<>();

    @Override
    public Optional<ChatUser> findById(UserId userId) {
        return Optional.ofNullable(store.get(userId));
    }

    @Override
    public void save(ChatUser chatUser) {
        store.put(chatUser.id(), chatUser);
    }
}
