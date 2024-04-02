package com.bookend.review.service;

import com.bookend.review.domain.entity.Review;
import com.bookend.review.domain.dto.ReviewRequestDto;
import com.bookend.review.domain.dto.ReviewResponseDto;
import com.bookend.review.repository.BookRepository;
import com.bookend.review.repository.ReviewRepository;
import com.bookend.security.domain.SessionUser;
import com.bookend.security.domain.dto.UserDetailsImpl;
import com.bookend.security.domain.entity.User;
import com.bookend.security.repository.UserRepository;
import groovy.lang.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.bookend.review.domain.entity.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // 독후감 목록 조회
    public Page<ReviewResponseDto> findAll(Pageable pageable, Long userId) {

        int page = pageable.getPageNumber();     // 현재 페이지
        int pageSize = pageable.getPageSize();   // 로드될 때마다 추가될 게시글 수

        // 독후감 목록 조회
        Page<Review> reviewList = reviewRepository.findByUserUserId(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "regDt")), userId);

        return reviewList.map(ReviewResponseDto::new); // entity -> dto
    }

    // 독후감 저장
    public Review save(ReviewRequestDto reviewRequestDto, SessionUser user) {
        Review review = new Review();

        if (user != null) {
            userRepository.findById(user.getUserId()).ifPresent(reviewRequestDto::setUser);

            Book book = bookRepository.findByIsbn(reviewRequestDto.getIsbn());        // 기존에 저장된 도서인지 확인
            if (book == null) book = bookRepository.save(new Book(reviewRequestDto)); // 기존에 저장된 도서가 아니면 저장
            reviewRequestDto.setBook(book);

            // 독후감 저장
            review = reviewRepository.save(new Review(reviewRequestDto));
        }else{
            log.error("user is null");
        }

        return review;
    }

    // 해당 review 조회
    public ReviewResponseDto findById(Long reviewId) {

        // reviewId를 이용하여 review 조회
        Review review = reviewRepository.findById(reviewId).orElse(null);

        return new ReviewResponseDto(review);
    }

    // 독후감 저장
    public Review edit(ReviewRequestDto reviewRequestDto) {

        // 수정할 독후감 가져오기
        Review review = reviewRepository.findById(reviewRequestDto.getReviewId()).orElse(null);

        // 독후감 수정
        if(review != null){
            review.edit(reviewRequestDto);
            reviewRepository.save(review);
        }

        return review;
    }

    // 독후감 삭제
    public void delete(Long reviewId, Long loginUserId) {

        // 삭제할 독후감 가져오기
        Review review = reviewRepository.findById(reviewId).orElse(null);

        // 로그인 유저와 작성자가 동일 인물일 경우 삭제
        if (review.getUser().getUserId() == loginUserId)
            reviewRepository.delete(review);

    }

    // 검색 키워드가 제목에 포함되어 있는 도서를 찾고 그 도서의 리뷰를 조회
    public Page<ReviewResponseDto> findByBook_TitleContaining(String searchReview,
                                                              Pageable pageable,
                                                              Long userId) {

        // 독후감 목록 조회
        Page<Review> reviewList = reviewRepository.findByBook_TitleContainingAndUserUserId(searchReview, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "regDt")), userId);

        return reviewList.map(ReviewResponseDto::new); // entity -> dto
    }

    // 달력에 뿌려줄 해당 년월의 리뷰 조회
    public List<ReviewResponseDto> findByYearAndMonth(int year, int month, Long userId) {

        List<Review> reviewList = reviewRepository.findByRegDtYearAndRegDtMonthAndUserUserId(year, month, userId);

        return reviewList.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    // 선택한 일자의 뿌려줄 독후감 목록 조회
    public List<ReviewResponseDto> findByYearAndMonthAndDay(int year, int month, int day, Long userId) {

        List<Review> reviewList = reviewRepository.findByYearAndMonthAndDayAndUserUserIdOrderByRegDtDesc(year, month, day, userId);

        return reviewList.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    // userId와 bookId로 독후감 조회
    public ReviewResponseDto findByUserUserIdAndBookBookId(Long senderUserId, Long senderBookId) {
        return new ReviewResponseDto();
    }
}
