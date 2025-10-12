package me.jeongrae.chat.interfaces.web;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String roomId;
    private String sender;
    private String content;
}