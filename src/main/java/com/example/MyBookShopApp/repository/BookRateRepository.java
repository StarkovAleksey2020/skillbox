package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.links.Book2RateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRateRepository extends JpaRepository<Book2RateEntity, Long> {

    Book2RateEntity findBook2RateEntitiesByBookEntity(BookEntity bookEntity);
}
