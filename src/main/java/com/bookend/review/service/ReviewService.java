package com.bookend.review.service;

import com.bookend.review.domain.dto.Review;
import com.bookend.review.domain.dto.ReviewResponseDto;
import com.bookend.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 독후감 목록 조회
    public List<ReviewResponseDto> findAll() {

        // 독후감 목록 조회
        List<Review> reviewList = reviewRepository.findAll();

        return ReviewResponseDto.toDtoList(reviewList);
    }
}
