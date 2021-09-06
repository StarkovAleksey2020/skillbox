package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.links.Book2RateEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRateRepository extends JpaRepository<Book2RateEntity, Long> {

    Book2RateEntity findBook2RateEntitiesByBookEntity(BookEntity bookEntity);

    @Query(value = "FROM Book2RateEntity br where br.userEntity = :userEntity and br.bookEntity = :bookEntity", nativeQuery = false)
    Integer findBook2RateEntityByUserEntityAndBookEntity(UserEntity userEntity, BookEntity bookEntity);

    @Query(value = "select * FROM book2rate br where br.user_id = :userId and br.book_id = :bookId", nativeQuery = true)
    Book2RateEntity findBook2Rate(Long userId, Long bookId);
}
