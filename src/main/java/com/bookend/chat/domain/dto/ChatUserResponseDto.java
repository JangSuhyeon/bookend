package com.bookend.chat.domain.dto;

import com.bookend.chat.domain.entity.ChatMessage;
import com.bookend.chat.domain.entity.ChatUser;
import com.bookend.review.domain.entity.Book;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
public class ChatUserResponseDto {

    private Long chatId;           // 채팅방 PK
    private Long userId;           // 참가자 PK
    private Date firstEnterTime;   // 처음 입장한 시간
    private Long bookId;           // 도서Id
    private String bookTitle;      // 도서명
    private String bookCover;      // 도서 사진
    private String lastChatMessage;// 마지막 채팅

    public ChatUserResponseDto(ChatUser chatUser) {
        this.chatId = chatUser.getChatId();
        this.userId = chatUser.getUserId();
        this.firstEnterTime = chatUser.getFirstEnterTime();
    }

    public void setBook(Book book) {
        this.bookId = book.getBookId();
        this.bookTitle = book.getTitle();
        this.bookCover = book.getCover();
    }

    public void setChat(ChatMessage chatMessage) {
        this.lastChatMessage = chatMessage.getMessage();
    }
}
