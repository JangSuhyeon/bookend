package com.bookend.review.service;

import com.bookend.review.domain.entity.Review;
import com.bookend.review.domain.dto.ReviewRequestDto;
import com.bookend.review.domain.dto.ReviewResponseDto;
import com.bookend.review.repository.BookRepository;
import com.bookend.review.repository.ReviewRepository;
import com.bookend.security.domain.SessionUser;
import com.bookend.security.domain.dto.UserDetailsImpl;
import com.bookend.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.bookend.review.domain.entity.Book;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // 독후감 목록 조회
    public List<ReviewResponseDto> findAll() {

        // 독후감 목록 조회
        List<Review> reviewList = reviewRepository.findAll();

        return ReviewResponseDto.toDtoList(reviewList); // entity -> dto
    }

    // 독후감 저장
    public void save(ReviewRequestDto reviewRequestDto, SessionUser user) {

        Long id;
        if (user != null) { // 구글 계정
            id = user.getUserId();
            reviewRequestDto.setUser(userRepository.findById(id).orElse(null)); // Todo null 예외 처리
        } else {
            id = null; // Todo 에러 예외 처리
        }

        Book book = bookRepository.findByIsbn(reviewRequestDto.getIsbn()); // 기존에 저장된 도서인지 확인
        if (book == null) {
            Book newBook = Book.toEntity(reviewRequestDto);
            book = bookRepository.save(newBook); // 기존에 저장된 도서가 아니면 저장
        }
        reviewRequestDto.setBook(book);

        // 독후감 저장
        Review review = Review.toEntity(reviewRequestDto);
        reviewRepository.save(review);
    }

    // 해당 review 조회
    public ReviewResponseDto findById(Long reviewId) {

        // reviewId를 이용하여 review 조회
        Review review = reviewRepository.findById(reviewId).orElseThrow(); // Todo 화면으로 보내지는 null 예외처리

        return ReviewResponseDto.toDto(review);
    }

    // 독후감 저장
    public void edit(ReviewRequestDto reviewRequestDto) {

        // 수정할 독후감 가져오기
        Review review = reviewRepository.findById(reviewRequestDto.getReviewId()).orElseThrow(); // Todo 화면으로 보내지는 null 예외처리

        // 독후감 수정
        review.edit(reviewRequestDto);
        reviewRepository.save(review);
    }

    // 독후감 삭제
    public void delete(Long reviewId) {

        // 삭제할 독후감 가져오기
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        reviewRepository.delete(review);

    }
}
