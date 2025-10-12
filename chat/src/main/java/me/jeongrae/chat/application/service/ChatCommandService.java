package me.jeongrae.chat.application.service;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.application.ChatUseCase;
import me.jeongrae.chat.application.SendMessageCommand;
import me.jeongrae.chat.application.port.ChatRoomRepository;
import me.jeongrae.chat.application.port.ChatUserRepository;
import me.jeongrae.chat.domain.model.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatCommandService implements ChatUseCase {

    private final ChatUserRepository chatUserRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public void join(UserId userId, String username) {
        if (chatUserRepository.findById(userId).isPresent()) {
            // Handle already connected user exception
            throw new IllegalStateException("User already connected");
        }
        ChatUser newUser = ChatUser.of(userId, username);
        chatUserRepository.save(newUser);
    }

    @Override
    public ChatRoom getOrCreateRoom(UserId user1, UserId user2) {
        RoomId roomId = RoomId.of(user1, user2);
        return chatRoomRepository.findById(roomId)
                .orElseGet(() -> {
                    ChatRoom newRoom = ChatRoom.of(user1, user2);
                    chatRoomRepository.save(newRoom);
                    return newRoom;
                });
    }

    @Override
    public ChatMessage sendMessage(SendMessageCommand command) {
        ChatRoom room = chatRoomRepository.findById(command.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (!room.canSend(command.getSenderId())) {
            throw new SecurityException("Sender is not a participant in this room");
        }

        ChatMessage message = ChatMessage.of(
                command.getRoomId(),
                command.getSenderId(),
                command.getContent()
        );

        room.addMessage(message);
        // In a real scenario, we would save the message and the room state.
        // chatRoomRepository.save(room);

        return message;
    }

    @Override
    public void leave(UserId userId) {
        // Implementation for leaving would go here.
        // For example, removing the user from any rooms they are in.
        // This is simplified for the prototype.
        System.out.println("User " + userId + " left.");
    }
}