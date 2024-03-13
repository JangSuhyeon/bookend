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
public class mainController {

    private final ReviewService reviewService;
    private final HttpSession httpSession;

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
            List<ReviewRequestDto> reviewRequestDtoList = ReviewRequestDto.dummyReviews();
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
}
