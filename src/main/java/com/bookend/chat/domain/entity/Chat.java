package com.bookend.chat.domain.entity;

import com.bookend.review.domain.dto.ReviewRequestDto;
import com.bookend.review.domain.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;  // PK

    @OneToOne
    @JoinColumn(name = "bookId")
    private Book book;  // 채팅방 주제 도서

    // dto -> entity
    public Chat(Book book) {
        this.book = book;
    }

}
