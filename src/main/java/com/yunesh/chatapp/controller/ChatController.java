package com.yunesh.chatapp.controller;

import com.yunesh.chatapp.dto.ChatMessage;
import com.yunesh.chatapp.model.ChatMessageEntity;
import com.yunesh.chatapp.repository.ChatMessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository messageRepository;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message) {
        message.setTimestamp(java.time.LocalTime.now().toString());

        // Save to DB
        ChatMessageEntity entity = ChatMessageEntity.builder()
                .sender(message.getSender())
                .recipient(message.getRecipient())
                .room(message.getRoom())
                .content(message.getContent())
                .type(message.getType())
                .timestamp(java.time.LocalDateTime.now())
                .build();
        messageRepository.save(entity);

        // Send to WebSocket
        if ("PRIVATE".equalsIgnoreCase(message.getType()) && message.getRecipient() != null) {
            messagingTemplate.convertAndSendToUser(
                    message.getRecipient(), "/queue/messages", message
            );
        } else {
            String destination = (message.getRoom() != null && !message.getRoom().isEmpty())
                    ? "/topic/room/" + message.getRoom()
                    : "/topic/messages";
            messagingTemplate.convertAndSend(destination, message);
        }
    }

    @MessageMapping("/history")
    public void getHistory(ChatMessage message) {
        List<ChatMessageEntity> messages =
                messageRepository.findByRoomOrderByTimestampAsc(message.getRoom());

        messages.forEach(m -> messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoom(),
                new ChatMessage(
                        m.getSender(),
                        m.getRecipient(),
                        m.getRoom(),
                        m.getContent(),
                        m.getType(),
                        m.getTimestamp().toString() // send back as string
                )
        ));
    }

}
