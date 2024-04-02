package com.bookend.chat.repository;

import com.bookend.chat.domain.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomChatRoomIdAndSendTimeAfter(Long chatId, Date firstEnterTime);

    ChatMessage findFirstByChatRoomChatRoomIdAndSendTimeAfterOrderBySendTimeDesc(Long chatId, Date enterDateTime);
}
