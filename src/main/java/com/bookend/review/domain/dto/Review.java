package com.bookend.review.domain.dto;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;       // PK

    @Column
    private Long bookId;         // 도서 PK
    private Long userId;         // 작성자 PK
    private int score;           // 도서 점수
    private String shortReview;  // 한줄평
    private String longReview;   // 독후감
    private Boolean openYn;      // 공개여부
    private Date modDt;          // 수정일
    private Date regDt;          // 작서일

    @PrePersist
    protected void setRegDt() {
        this.regDt = new Date(); // Review를 저장하기 전 작성일 등록
    }
}
