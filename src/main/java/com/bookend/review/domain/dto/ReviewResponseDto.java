package com.bookend.review.domain.dto;

import com.bookend.review.domain.entity.Book;
import com.bookend.review.domain.entity.Review;
import com.bookend.security.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
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

    // 리스트를 쪼개어 dto로 변환
    public static List<ReviewResponseDto> toDtoList(List<Review> reviewList) {
        return reviewList.stream()
                .map(ReviewResponseDto::toDto)
                .collect(Collectors.toList());
    }

    // entity -> dto
    public static ReviewResponseDto toDto(Review review) {

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");

        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .score(review.getScore())
                .shortReview(review.getShortReview())
                .longReview(review.getLongReview())
                .openYn(review.getOpenYn())
                .modDt(review.getModDt())
                .regDt(format.format(review.getRegDt())) // 날짜 형식 변경
                .book(review.getBook())
                .user(review.getUser())
                .build();
    }
}
