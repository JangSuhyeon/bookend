package com.bookend.review.repository;

import com.bookend.review.domain.dto.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
