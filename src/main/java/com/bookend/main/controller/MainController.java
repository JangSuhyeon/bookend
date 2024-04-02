package com.bookend.main.controller;

import com.bookend.review.domain.dto.ReviewRequestDto;
import com.bookend.review.domain.dto.ReviewResponseDto;
import com.bookend.review.service.ReviewService;
import com.bookend.security.domain.SessionUser;
import com.bookend.security.domain.annotation.LoginUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class MainController {

    private final ReviewService reviewService;

    // 독후감 목록 페이지로 이동
    @GetMapping("")
    public String goToIndex(@PageableDefault(page = 0) Pageable pageable,
                            Model model,
                            @LoginUser SessionUser loginUser) {

        long reviewsCnt;

        // 독후감 목록 조회
        pageable = PageRequest.of(pageable.getPageNumber(), 10);
        Page<ReviewResponseDto> reviewList = reviewService.findAll(pageable, loginUser.getUserId());
        reviewsCnt = reviewList.getTotalElements();

        // review가 한개도 없을 때 더미 데이터 insert
        if (reviewsCnt == 0){
            List<ReviewRequestDto> reviewRequestDtoList = createDummyReviews();
            for (ReviewRequestDto reviewRequestDto : reviewRequestDtoList) {
                reviewService.save(reviewRequestDto, loginUser);
            }
            reviewList = reviewService.findAll(pageable, loginUser.getUserId());
            reviewsCnt = reviewList.getTotalElements();
        }

        model.addAttribute("page", pageable);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("reviewsCount", reviewsCnt);
        model.addAttribute("loginUser", loginUser);

        return "index";
    }

    // 더미 데이터 만들기
    public List<ReviewRequestDto> createDummyReviews(){
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
