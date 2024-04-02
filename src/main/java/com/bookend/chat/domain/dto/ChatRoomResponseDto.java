package com.bookend.chat.domain.dto;

import com.bookend.chat.domain.entity.ChatMessage;
import com.bookend.chat.domain.entity.ChatRoom;
import com.bookend.review.domain.entity.Book;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatRoomResponseDto {

    private Long chatRoomId;

    // Book
    private Long bookId;             // PK
    private String title;            // 도서명
    private String isbn;             // 도서고유번호
    private String author;           // 작가
    private String publisher;        // 출판사
    private String cover;            // 책커버 주소

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getChatRoomId();

        Book book = chatRoom.getBook();
        this.bookId = book.getBookId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.cover = book.getCover();
    }
}
