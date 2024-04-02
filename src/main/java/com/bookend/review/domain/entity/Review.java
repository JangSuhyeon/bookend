package com.bookend.review.domain.entity;

import com.bookend.review.domain.dto.ReviewRequestDto;
import com.bookend.security.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@ToString
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
    private String shortReview;  // 한줄평
    private String longReview;   // 독후감
    private int score;           // 점수
    private Boolean openYn;      // 공개여부
    private Date regDt;          // 작성일
    private Date modDt;          // 수정일

    @ManyToOne
    @JoinColumn(name = "bookId", nullable = false) // book_id 컬럼을 외래키로 지정, not null
    private Book book;           // 도서

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;           // 작성자

    // dto -> entity
    public Review (ReviewRequestDto dto) {
        this.shortReview = dto.getShortReview();
        this.longReview = dto.getLongReview();
        this.score = dto.getScore();
        this.openYn = dto.getOpenYn();
        this.book = dto.getBook();
        this.user = dto.getUser();
        this.regDt = new Date();
    }

    // 독후감 수정
    public void edit(ReviewRequestDto dto) {
        this.openYn = dto.getOpenYn();
        this.score = dto.getScore();
        this.shortReview = dto.getShortReview();
        this.longReview = dto.getLongReview();
        this.modDt = new Date();
    }

}
