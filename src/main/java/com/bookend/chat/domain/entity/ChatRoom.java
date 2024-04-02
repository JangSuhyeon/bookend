package com.bookend.chat.domain.entity;

import com.bookend.review.domain.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;  // PK

    @OneToOne
    @JoinColumn(name = "bookId", nullable = false)
    private Book book;    // 도서

    // dto -> entity
    public ChatRoom(Book book) {
        this.book = book;
    }

}
