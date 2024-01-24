package com.bookend.review.controller;

import com.bookend.review.domain.dto.ReviewRequestDto;
import com.bookend.review.domain.dto.ReviewResponseDto;
import com.bookend.review.service.ReviewService;
import com.bookend.security.domain.annotation.LoginUser;
import com.bookend.security.domain.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Value("${aladin.url}")
    private String ALADIN_URL;

    private final HttpSession httpSession;

    // 독후감 작성 페이지로 이동
    @GetMapping("/write")
    public String goToWrite(@LoginUser SessionUser loginUser,
                            Model model) {

        model.addAttribute("loginUser", loginUser); // login user

        return "review/write";
    }

    // 알라딘 Open API를 이용하여 도서 검색
    @PostMapping(value = "/book/search", produces = "application/json")
    public ResponseEntity<String> searchBooks(@RequestBody HashMap<String, Object> search) {
        RestTemplate restTemplate = new RestTemplate();

        // 도서 검색 결과 요청
        URI aladinUri = UriComponentsBuilder
                .fromUriString(ALADIN_URL)
                .queryParam("Query", search.get("searchKeyword"))
                .queryParam("QueryType", "Title")
                .queryParam("MaxResults", "10")
                .queryParam("start", "1")
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "JS")
                .queryParam("Version", "20131101")
                .build()
                .encode(StandardCharsets.UTF_8).toUri();

        return restTemplate.getForEntity(aladinUri, String.class);
    }

    // 독후감 저장
    @PostMapping("/write")
    public ResponseEntity<String> saveReview(@RequestBody ReviewRequestDto reviewRequestDto,
                                             @LoginUser SessionUser loginUser) {

        reviewService.save(reviewRequestDto, loginUser); // login user

        return ResponseEntity.ok("success"); // todo 예외처리 필요
    }

    // 독후감 작성 페이지로 이동
    @GetMapping("/{reviewId}")
    public String goToWrite(@PathVariable("reviewId")Long reviewId,
                            @LoginUser SessionUser loginUser,
                            Model model) {

        // 해당 review 조회
        ReviewResponseDto review = reviewService.findById(reviewId);

        model.addAttribute("review", review);
        model.addAttribute("loginUser", loginUser);

        return "review/detail";
    }

}
