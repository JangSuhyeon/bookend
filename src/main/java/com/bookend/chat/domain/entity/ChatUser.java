package com.bookend.chat.domain.entity;

import com.bookend.security.domain.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ToString
@NoArgsConstructor
@Getter
@Entity
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatUserId;

    @Column
    private Long chatId;
    private Long userId;
    private Date firstEnterTime;

    public ChatUser (Long chatId, Long userId) {
        this.chatId = chatId;
        this.userId = userId;
    }

    @PrePersist
    protected void setFirstEnterTime() {
        this.firstEnterTime = new Date();
    }

}
