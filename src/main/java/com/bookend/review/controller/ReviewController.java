package com.bookend.review.controller;

import com.bookend.review.domain.dto.ReviewRequestDto;
import com.bookend.review.domain.dto.ReviewResponseDto;
import com.bookend.review.service.ReviewService;
import com.bookend.security.domain.annotation.LoginUser;
import com.bookend.security.domain.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Value("${aladin.url}")
    private String ALADIN_URL;

    private final HttpSession httpSession;

    // 독후감 추가 로드
    @ResponseBody
    @PostMapping(value = "/load", produces = "application/json; charset=UTF-8")
    public HashMap<String, Object> reviewSearch(@RequestBody HashMap<String, String> searchReview) {
        HashMap<String, Object> result = new HashMap<>();
        Page<ReviewResponseDto> reviewList;

        Pageable pageable = PageRequest.of(Integer.parseInt(searchReview.get("page"))+1, Integer.parseInt(searchReview.get("pageSize"))); // 다음 페이지 조회

        // 검색된 독후감 목록
        String searchKeyword = searchReview.get("searchReview");
        if (searchKeyword.isBlank()){
            reviewList = reviewService.findAll(pageable); // 검색어 없을 때
        }else{
            reviewList = reviewService.findByBook_TitleContaining(searchReview.get("searchReview"), pageable); // 검색어 있을 때
        }

        result.put("reviewList", reviewList);
        result.put("page", pageable);

        return result;
    }

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

    // 독후감 저장
    @PostMapping("/write")
    public ResponseEntity<String> saveReview(@RequestBody ReviewRequestDto reviewRequestDto,
                                             @LoginUser SessionUser loginUser) {

        reviewService.save(reviewRequestDto, loginUser); // login user

        return ResponseEntity.ok("success"); // todo 예외처리 필요
    }

    // 독후감 수정 페이지로 이동
    @GetMapping("/{reviewId}/edit")
    public String goToEdit(@PathVariable("reviewId") Long reviewId,
                           @LoginUser SessionUser loginUser,
                           Model model) {

        // 해당 review 조회
        ReviewResponseDto review = reviewService.findById(reviewId);

        model.addAttribute("review", review);
        model.addAttribute("loginUser", loginUser);

        return "review/edit";
    }

    // 독후감 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<String> editReview(@RequestBody ReviewRequestDto reviewRequestDto) {

        reviewService.edit(reviewRequestDto);

        return ResponseEntity.ok("success");
    }

    // 독후감 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long reviewId,
                                               @LoginUser SessionUser loginUser) {
        // Todo 현재 로그인한 user와 작성자가 동일인인지 확인 필요

        reviewService.delete(reviewId);

        return ResponseEntity.ok("success");
    }

    // 달력에 뿌려줄 해당 일자의 리뷰 조회
    @PostMapping("/calendar")
    @ResponseBody
    public HashMap calendar(@RequestParam("year")int year,
                            @RequestParam("month")int month,
                            @RequestParam(value = "day", required = false) Optional<Integer> optionalDay) { // 일자를 보낼수도 있고 안 보낼수도 있기 떄문에 Optional<>로 받음

        HashMap<String,List> result = new HashMap<>();
        List<ReviewResponseDto> reviewList = new ArrayList<>();
        System.out.println("date");
        System.out.println(year);
        System.out.println(month);
        if(optionalDay.isPresent()){ // day가 있다면 년월일로 검색
            int day = optionalDay.get();
            System.out.println(day);
            reviewList = reviewService.findByYearAndMonthAndDay(year, month, day);
        }else{                       // day가 없으면 년월로 검색
            reviewList = reviewService.findByYearAndMonth(year, month);
        }

        result.put("reviewList", reviewList);
        for (ReviewResponseDto reviewResponseDto : reviewList) {
            System.out.println(reviewResponseDto);
        }

        return result;
    }
}
