package com.bookend.review.repository;

import com.bookend.review.domain.entity.Book;
import com.bookend.review.domain.entity.Review;
import groovy.lang.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByIsbn(String isbn);

    List<Book> findByTitleContaining(String searchReview);
}
