package com.bookend.review.domain.entity;

import com.bookend.review.domain.dto.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor   // 객체를 생성한 후에 빌더 패턴을 통해 필드를 설정 (없으면 객체를 생성할 때 문제가 발생)
@AllArgsConstructor  // 빌더 패턴을 통해 객체를 생성할 때 필드 값을 설정 (없으면 값을 설정하는 데 제약)
@Builder
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column
    private String title;
    private String author;
    private String publisher;
    private String cover;

    // dto -> entity
    public static Book toEntity(ReviewRequestDto dto) {
        return Book.builder()
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .cover(dto.getCover())
                .build();
    }
}
