package com.bookend.chat.repository;

import com.bookend.chat.domain.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    ChatUser findByUserIdAndChatId(Long userId, Long chatId);

    List<ChatUser> findByuserId(Long userId);
}
