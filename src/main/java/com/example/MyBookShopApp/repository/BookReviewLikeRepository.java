package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface BookReviewLikeRepository extends JpaRepository<BookReviewLikeEntity, Long> {

    @Query(value = "FROM BookReviewLikeEntity brl WHERE brl.user = :user")
    List<BookReviewLikeEntity> getBookReviewLikeEntitiesByUser(@RequestParam("user") UserEntity user);

    @Query(value = "FROM BookReviewLikeEntity brl WHERE brl.reviewId = :id")
    List<BookReviewLikeEntity> getBookReviewLikeEntitiesByUser(@RequestParam("id") Long id);

    @Query(value = "SELECT count(*) FROM book_review_like brl WHERE brl.review_id = :reviewId AND brl.value = 1", nativeQuery = true)
    Integer getLikesCount(@RequestParam("reviewId") Long reviewId);

    @Query(value = "SELECT count(*) FROM book_review_like brl WHERE brl.review_id = :reviewId AND brl.value = 0", nativeQuery = true)
    Integer getDislikesCount(@RequestParam("reviewId") Long reviewId);

    @Query(value = "SELECT * FROM book_review_like brl WHERE brl.review_id = :reviewId AND brl.user_id = :userId" , nativeQuery = true)
    BookReviewLikeEntity getReviewLikeEntityByReviewIdAndUserId(@RequestParam("reviewId") Long reviewId,
                                                              @RequestParam("userId") Long userId);

}
