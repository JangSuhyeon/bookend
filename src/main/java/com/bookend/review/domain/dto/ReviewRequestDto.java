package com.bookend.review.domain.dto;

import com.bookend.review.domain.entity.Book;
import com.bookend.review.domain.entity.Review;
import com.bookend.security.domain.entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class ReviewRequestDto {

    private Long reviewId;              // 독후감 PK (독후감 수정/삭제 시 이용)
    private String shortReview;      // 한줄평
    private String longReview;       // 독후감
    private int score;               // 도서 점수
    private Boolean openYn;          // 공개여부
    private Date regDt;              // 작성일
    private Date modDt;              // 수정일

    // Book
    private Long bookId;             // PK
    private String title;            // 도서명
    private String isbn;             // 도서고유번호
    private String author;           // 작가
    private String publisher;        // 출판사
    private String cover;            // 책커버 주소

    // 저장을 위함
    private Book book;
    private User user;
}
