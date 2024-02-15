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
    private Long bookId;            // 도서 PK
    private Long userId;            // 작성자 PK

    private int score;              // 도서 점수
    private String shortReview;     // 한줄평
    private String longReview;      // 독후감
    private Boolean openYn;         // 공개여부
    private Date modDt;             // 수정일
    private String regDt;           // 작성일

    private Book book;              // 도서
    private User user;              // 작성자

    // entity -> dto
    public ReviewResponseDto(Review review) {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");

        this.reviewId = review.getReviewId();
        this.score = review.getScore();
        this.shortReview = review.getShortReview();
        this.longReview = review.getLongReview();
        this.openYn = review.getOpenYn();
        this.modDt = review.getModDt();
        this.regDt = format.format(review.getRegDt()); // 날짜 형식 변경
        this.book = review.getBook();
        this.user = review.getUser();
    }
}
