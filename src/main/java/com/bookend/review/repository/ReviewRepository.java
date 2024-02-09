package com.bookend.review.repository;

import com.bookend.review.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByOrderByRegDtDesc();

    Page<Review> findByBook_TitleContaining(String searchReview, PageRequest pageable);
}
