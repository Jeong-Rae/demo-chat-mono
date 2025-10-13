package me.jeongrae.chat.infrastructure.persistence;

import me.jeongrae.chat.application.port.ChatRoomRepository;
import me.jeongrae.chat.domain.chat.model.ChatRoom;
import me.jeongrae.chat.domain.chat.model.RoomId;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryChatRoomRepository implements ChatRoomRepository {

    private final Map<RoomId, ChatRoom> store = new ConcurrentHashMap<>();

    @Override
    public Optional<ChatRoom> findById(RoomId roomId) {
        return Optional.ofNullable(store.get(roomId));
    }

    @Override
    public void save(ChatRoom chatRoom) {
        store.put(chatRoom.id(), chatRoom);
    }
}
