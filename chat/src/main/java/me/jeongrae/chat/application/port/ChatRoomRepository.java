package me.jeongrae.chat.application.port;

import me.jeongrae.chat.domain.model.ChatRoom;
import me.jeongrae.chat.domain.model.RoomId;

import java.util.Optional;

public interface ChatRoomRepository {
    Optional<ChatRoom> findById(RoomId roomId);

    void save(ChatRoom chatRoom);
}
