package com.bookend.main.controller;

import com.bookend.review.domain.dto.ReviewResponseDto;
import com.bookend.review.service.ReviewService;
import com.bookend.security.domain.SessionUser;
import com.bookend.security.domain.annotation.LoginUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class mainController {

    private final ReviewService reviewService;
    private final HttpSession httpSession;

    // 독후감 목록 페이지로 이동
    @GetMapping("")
    public String goToIndex(Model model,
                            @LoginUser SessionUser loginUser) {

        // 독후감 목록 조회
        List<ReviewResponseDto> reviewList = reviewService.findAll();

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("loginUser", loginUser);

        return "index";
    }
}
