package com.bookend.chat.domain.entity;

import com.bookend.chat.domain.dto.ChatMessageDto;
import com.bookend.security.domain.Role;
import com.bookend.security.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @Column
    private Long chatId;
    private String message;
    private Boolean firstEntry;
    private Date sendTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMessageDto.MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @PrePersist
    protected void setSendTime() {
        this.sendTime = new Date();
    }
}
