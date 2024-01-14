package com.bookend.review.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {

    private Long UserId;        // 작성자 id
    private long BookId;        // 도서 id

    private Boolean openYn;     // 공개여부
    private int score;          // 도서 점수
    private String shortReview; // 한줄평
    private String longReview;  // 독후감

    private String isbn;        // 도서를 구분할 수 있는 고유번호
    private String title;       // 도서 제목
    private String author;      // 작가
    private String publisher;   // 출판사
    private String cover;       // 커버 이미지
}
