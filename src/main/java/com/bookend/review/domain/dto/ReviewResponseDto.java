package com.bookend.review.domain.dto;

import com.bookend.review.domain.entity.Book;
import com.bookend.review.domain.entity.Review;
import com.bookend.security.domain.entity.User;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    private Long reviewId;          // 독후감 PK
    private String shortReview;     // 한줄평
    private String longReview;      // 독후감
    private int score;              // 점수
    private Boolean openYn;         // 공개여부
    private String regDt;           // 작성일
    private Date modDt;             // 수정일

    // Book
    private Long bookId;             // PK
    private String title;            // 도서명
    private String isbn;             // 도서고유번호
    private String author;           // 작가
    private String publisher;        // 출판사
    private String cover;            // 책커버 주소

    // User
    private Long userId;             // PK
    private String name;             // 회원이름

    // entity -> dto
    public ReviewResponseDto(Review review) {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");

        this.reviewId = review.getReviewId();
        this.shortReview = review.getShortReview();
        this.longReview = review.getLongReview();
        this.score = review.getScore();
        this.openYn = review.getOpenYn();
        this.regDt = format.format(review.getRegDt()); // 날짜 형식 변경
        this.modDt = review.getModDt();

        Book book = review.getBook();
        this.bookId = book.getBookId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.cover = book.getCover();

        User user = review.getUser();
        this.userId = user.getUserId();
        this.name = user.getName();
    }
}
