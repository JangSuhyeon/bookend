package com.bookend.review.domain.entity;

import com.bookend.review.domain.dto.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL) // 양방향 매핑에서 book이 주인 엔터티, book 엔티티에 대한 어떤 변경이든 연관된 엔티티에도 동일하게 적용
    private List<Review> review;                             // 양방향 매핑은 한 엔티티에서 다른 엔티티로의 참조를 가지고 있을 때, 그 반대 역시 참조할 수 있도록 설정하는 것

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
