package com.yunesh.chatapp.controller;

import com.yunesh.chatapp.dto.ChatMessage;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat") // Client sends it to: /app/chat
    public void sendMessage(ChatMessage message) {
        message.setTimestamp(java.time.LocalDateTime.now().toString());

        if ("PRIVATE".equalsIgnoreCase(message.getType()) && message.getRecipient() != null) {
            // Direct 1-to-1 message
            messagingTemplate.convertAndSendToUser(
                    message.getRecipient(), "/queue/messages", message
            );
        } else {
            // Room-based broadcast (if room is set) or fallback to general
            String destination = (message.getRoom() != null && !message.getRoom().isEmpty())
                    ? "/topic/room/" + message.getRoom()
                    : "/topic/messages";

            messagingTemplate.convertAndSend(destination, message);
        }
    }
}
