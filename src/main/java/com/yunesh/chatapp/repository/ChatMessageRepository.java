package com.yunesh.chatapp.repository;

import com.yunesh.chatapp.model.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByRoomOrderByTimestampAsc(String room);
    List<ChatMessageEntity> findBySenderAndRecipientOrderByTimestampAsc(String sender, String recipient);
}
