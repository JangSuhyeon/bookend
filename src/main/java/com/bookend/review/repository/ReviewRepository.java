package com.bookend.review.repository;

import com.bookend.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByOrderByRegDtDesc();

    List<Review> findByBook_TitleContainingOrderByRegDtDesc(String searchReview);
}
