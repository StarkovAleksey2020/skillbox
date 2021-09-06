package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.BookReviewRLDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface BookReviewRLDtoRepository extends JpaRepository<BookReviewRLDto, Long> {

    @Query(value = "select br.id as id, br.time as reviewTime, br.text as textReview, ue.name as userName, br2.rate as userBookRate, count(*) filter (where brl.value = 1) as reviewLikesCount, count(*) filter (where brl.value = 0) as reviewDislikesCount from book_review br join user_entity ue on br.user_id = ue.id join book_review_like brl on brl.review_id = br.id left join book2rate br2 on br2.book_id = br.book_id and br2.user_id = br.user_id where br.book_id = :bookId group by br.id, br.time, br.text, ue.name, br2.rate", nativeQuery = true)
    List<BookReviewRLDto> findBookReviewInfoByBookId(@RequestParam("bookId") Long bookId);

    @Query(value = "select br.*, ue.* from book_review br join user_entity ue on br.user_id = ue.id where br.book_id = :bookId", nativeQuery = true)
    List<BookReviewRLDto> findBookReviewInfoByBookId1(@RequestParam("bookId") Long bookId);
}
