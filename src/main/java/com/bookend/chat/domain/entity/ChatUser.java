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
    private Date enterDateTime;
    private Boolean outYn;

    @ManyToOne
    @JoinColumn(name = "chatId", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @PrePersist
    protected void setEnterDateTime() {
        this.enterDateTime = new Date();
    }

    public ChatUser (ChatRoom chatRoom, User user) {
        this.outYn = false;
        this.chatRoom = chatRoom;
        this.user = user;
    }

    // 채팅방 나가기 상태 변경
    public void setOutYn(boolean outYn) {
        this.outYn = outYn;
    }

}
