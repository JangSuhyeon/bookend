package com.bookend.review.domain.dto;

import com.bookend.review.domain.entity.Book;
import com.bookend.security.domain.entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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

    // 더미 데이터 만들기
    public static List<ReviewRequestDto> dummyReviews() {
        List<ReviewRequestDto> reviewRequestDtoList = new ArrayList<>();
        ReviewRequestDto dto1 = ReviewRequestDto.builder()
                .openYn(true)
                .score(5)
                .shortReview("샘플 한줄평 입니다.")
                .longReview("샘플 독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.")
                .isbn("8963475824")
                .title("앨리스의 이상한 나라 경제학 퇴치 가이드 - 정치인과 대중을 위한 새 경제학 여행")
                .author("현동균 (지은이)")
                .publisher("진인진")
                .cover("https://image.aladin.co.kr/product/33080/45/coversum/8963475824_1.jpg")
                .build();
        reviewRequestDtoList.add(dto1);
        ReviewRequestDto dto2 = ReviewRequestDto.builder()
                .openYn(false)
                .score(4)
                .shortReview("샘플 한줄평 입니다.")
                .longReview("샘플 독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.")
                .isbn("K642935143")
                .title("해리 포터와 아즈카반의 죄수 (미나리마 에디션)")
                .author("J.K. 롤링 (지은이), 미나리마 (그림), 강동혁 (옮긴이)")
                .publisher("문학수첩")
                .cover("https://image.aladin.co.kr/product/32575/8/coversum/k642935143_1.jpg")
                .build();
        reviewRequestDtoList.add(dto2);
        ReviewRequestDto dto3 = ReviewRequestDto.builder()
                .openYn(true)
                .score(3)
                .shortReview("샘플 한줄평 입니다.")
                .longReview("샘플 독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.독후감 입니다.")
                .isbn("8980714548")
                .title("입이 큰 개구리 - 개정판")
                .author("키스 포크너 (지은이), 조너선 램버트 (그림), 정채민 (옮긴이)")
                .publisher("미세기")
                .cover("https://image.aladin.co.kr/product/19163/64/coversum/8980714548_2.jpg")
                .build();
        reviewRequestDtoList.add(dto3);
        return reviewRequestDtoList;
    }
}
