package com.bookend.review.repository;

import com.bookend.review.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByBook_TitleContainingAndUserUserId(String searchReview, PageRequest pageable, Long userId);

    @Query("SELECT e FROM Review e WHERE YEAR(e.regDt) = :year AND MONTH(e.regDt) = :month AND e.user.userId = :userId")
    List<Review> findByRegDtYearAndRegDtMonthAndUserUserId(int year, int month, Long userId);

    @Query("SELECT e FROM Review e WHERE YEAR(e.regDt) = :year AND MONTH(e.regDt) = :month AND DAY(e.regDt) = :day AND e.user.userId = :userId")
    List<Review> findByYearAndMonthAndDayAndUserUserIdOrderByRegDtDesc(int year, int month, int day, Long userId);

    Page<Review> findByUserUserId(PageRequest regDt, Long userId);

    Review findByUserUserIdAndBookBookId(Long senderUserId, Long senderBookId);
}
