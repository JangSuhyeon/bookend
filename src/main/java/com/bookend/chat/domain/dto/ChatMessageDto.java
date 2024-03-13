package com.bookend.chat.domain.dto;

import com.bookend.chat.domain.entity.ChatMessage;
import com.bookend.security.domain.entity.User;
import com.nimbusds.oauth2.sdk.util.date.SimpleDate;
import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private MessageType messageType; // 메시지 타입
    private Long chatId;             // 방 id
    private Long userId;             // 수신자 id
    private User user;               // 수신자 정보
    private String message;          // 메시지
    private String sendTime;         // 수신 시간
    private String sendDay;          // 수신 일자
    private Boolean firstEntry;      // 첫 입장 여부

    public ChatMessageDto(ChatMessage chatMessage) {
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDay = new SimpleDateFormat("yyyy년 M월 d일");

        this.chatId = chatMessage.getChatId();
        this.user = chatMessage.getUser();
        this.messageType = chatMessage.getMessageType();
        this.message = chatMessage.getMessage();
        this.firstEntry = chatMessage.getFirstEntry();
        this.sendTime = formatTime.format(chatMessage.getSendTime());
        this.sendDay = formatDay.format(chatMessage.getSendTime());
    }

}
