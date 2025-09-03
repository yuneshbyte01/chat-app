package com.yunesh.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String sender;
    private String content;
    private String type;      // CHAT, JOIN, LEAVE, PRIVATE
    private String timestamp;
    private String recipient; // NEW: username of the target recipient (for private chat)
    private String room;    // NEW: chat room identifier (for group chats)
}
