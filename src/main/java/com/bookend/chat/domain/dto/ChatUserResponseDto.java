package com.bookend.chat.domain.dto;

import com.bookend.chat.domain.entity.ChatRoom;
import com.bookend.chat.domain.entity.ChatMessage;
import com.bookend.chat.domain.entity.ChatUser;
import com.bookend.review.domain.entity.Book;
import com.bookend.security.domain.entity.User;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
public class ChatUserResponseDto {

    private Long chatUserId;         // PK
    private Date enterDateTime;      // 채팅방 입장 시간

    private String lastChatMessage;  // 마지막 채팅

    // ChatRoom
    private Long chatRoomId;         // PK

    // Book
    private Long bookId;             // PK
    private String title;            // 도서명
    private String isbn;             // 도서고유번호
    private String author;           // 작가
    private String publisher;        // 출판사
    private String cover;            // 책커버 주소

    // User
    private Long userId;             // PK
    private String name;             // 회원이름

    public ChatUserResponseDto(ChatUser chatUser) {
        this.chatUserId = chatUser.getChatUserId();
        this.enterDateTime = chatUser.getEnterDateTime();

        ChatRoom chatRoom = chatUser.getChatRoom();
        Book book = chatRoom.getBook();
        this.chatRoomId = chatRoom.getChatRoomId();
        this.bookId = book.getBookId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.cover = book.getCover();

        User user = chatUser.getUser();
        this.userId = user.getUserId();
        this.name = user.getName();
    }

    public void setLastChatMessage(ChatMessage chatMessage) {
        this.lastChatMessage = chatMessage.getMessage();
    }
}
