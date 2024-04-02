package com.bookend.review.domain.entity;

import com.bookend.review.domain.dto.ReviewRequestDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor   // 객체를 생성한 후에 빌더 패턴을 통해 필드를 설정 (없으면 객체를 생성할 때 문제가 발생)
@AllArgsConstructor  // 빌더 패턴을 통해 객체를 생성할 때 필드 값을 설정 (없으면 값을 설정하는 데 제약)
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bookId")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;        // PK

    @Column
    private String title;       // 도서명
    private String isbn;        // 도서고유번호
    private String author;      // 작가
    private String publisher;   // 출판사
    private String cover;       // 책커버 주소

    // dto -> entity
    public Book (ReviewRequestDto dto) {
        this.title = dto.getTitle();
        this.isbn = dto.getIsbn();
        this.author = dto.getAuthor();
        this.publisher = dto.getPublisher();
        this.cover = dto.getCover();
    }
}
