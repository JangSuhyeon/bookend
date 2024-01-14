package com.bookend.review.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<ReviewResponseDto> toDtoList(List<Review> reviewList) {
        return reviewList.stream()
                .map(ReviewResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public static ReviewResponseDto toDto(Review review) {

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");

        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .bookId(review.getBookId())
                .userId(review.getUserId())
                .score(review.getScore())
                .shortReview(review.getShortReview())
                .longReview(review.getLongReview())
                .openYn(review.getOpenYn())
                .modDt(review.getModDt())
                .regDt(format.format(review.getRegDt()))
                .build();
    }
}
