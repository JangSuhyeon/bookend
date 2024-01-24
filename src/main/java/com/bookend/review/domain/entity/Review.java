package com.bookend.review.domain.entity;

import com.bookend.review.domain.dto.ReviewRequestDto;
import com.bookend.security.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;       // PK

    @Column
//    private Long userId;         // 작성자 PK
    private int score;           // 도서 점수
    private String shortReview;  // 한줄평
    private String longReview;   // 독후감
    private Boolean openYn;      // 공개여부
    private Date modDt;          // 수정일
    private Date regDt;          // 작성일

    @ManyToOne
    @JoinColumn(name = "bookId", nullable = false) // book_id 컬럼을 외래키로 지정, not null
    private Book book;           // 도서

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;           // 작성자

    @PrePersist
    protected void setRegDt() {
        this.regDt = new Date(); // Review를 저장하기 전 작성일 등록
    }

    // dto -> entity
    public static Review toEntity(ReviewRequestDto dto) {
        return Review.builder()
//                .userId(dto.getUserId())
                .score(dto.getScore())
                .shortReview(dto.getShortReview())
                .longReview(dto.getLongReview())
                .openYn(dto.getOpenYn())
                .book(dto.getBook())
                .user(dto.getUser())
                .build();
    }
}
