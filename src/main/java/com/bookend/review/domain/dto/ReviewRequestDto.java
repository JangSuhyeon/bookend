package com.bookend.review.domain.dto;

import com.bookend.review.domain.entity.Book;
import com.bookend.security.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {

    private Long reviewId;      // 독후감 PK (독후감 수정/삭제 시 이용)
    private Boolean openYn;     // 공개여부
    private int score;          // 도서 점수
    private String shortReview; // 한줄평
    private String longReview;  // 독후감

    private String isbn;        // 도서를 구분할 수 있는 고유번호
    private String title;       // 도서 제목
    private String author;      // 작가
    private String publisher;   // 출판사
    private String cover;       // 커버 이미지

    private Book book;
    private User user;
}
