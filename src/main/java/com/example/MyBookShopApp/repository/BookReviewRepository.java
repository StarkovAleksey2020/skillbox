package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.review.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Long> {

    @Query(value = "FROM BookReviewEntity br WHERE br.bookEntity = :bookEntity")
    List<BookReviewEntity> findByBookEntity(@RequestParam("bookEntity") BookEntity bookEntity);

    @Query(value = "select * FROM book_review br WHERE br.book_id = :bookId", nativeQuery = true)
    List<BookReviewEntity> findByBookId(@RequestParam("bookId") Long bookId);

}
