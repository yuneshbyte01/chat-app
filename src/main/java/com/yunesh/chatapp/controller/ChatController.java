package com.yunesh.chatapp.controller;

import com.yunesh.chatapp.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat")       // Client sends it to: /app/chat
    @SendTo("/topic/messages")     // Server broadcasts to: /topic/messages
    public ChatMessage sendMessage(ChatMessage message) {
        // Add timestamp when a message is received
        message.setTimestamp(java.time.LocalDateTime.now().toString());
        return message;
    }
}

