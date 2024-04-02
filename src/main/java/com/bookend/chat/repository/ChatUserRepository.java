package com.bookend.chat.repository;

import com.bookend.chat.domain.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    ChatUser findByUserUserIdAndChatRoomChatRoomIdAndOutYn(Long userId, Long chatId, boolean outYn);

    List<ChatUser> findByUserUserIdAndOutYn(Long userId, boolean outYn);
}
