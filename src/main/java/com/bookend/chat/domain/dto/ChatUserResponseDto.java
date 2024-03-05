package com.bookend.chat.domain.dto;

import java.util.Date;

public class ChatUserResponseDto {

    private Long chatId;           // 채팅방 PK
    private Long userId;           // 참가자 PK
    private Date firstEnterTime;    // 처음 입장한 시간

}
