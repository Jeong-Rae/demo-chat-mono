package me.jeongrae.chat.interfaces.web;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.application.ChatUseCase;
import me.jeongrae.chat.application.SendMessageCommand;
import me.jeongrae.chat.domain.model.ChatMessage;
import me.jeongrae.chat.domain.model.ChatText;
import me.jeongrae.chat.domain.model.RoomId;
import me.jeongrae.chat.domain.model.UserId;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatUseCase chatUseCase;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(SendMessageRequest request) {
        SendMessageCommand command = new SendMessageCommand(
                RoomId.of(request.getRoomId()),
                UserId.of(request.getSender()),
                ChatText.of(request.getContent())
        );

        ChatMessage chatMessage = chatUseCase.sendMessage(command);

        // Broadcast the message to the room topic
        messagingTemplate.convertAndSend("/topic/room." + chatMessage.roomId().value(), chatMessage);
    }
}