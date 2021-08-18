package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.book.links.Book2TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2TagRepository extends JpaRepository<Book2TagEntity, Long> {
    Book2TagEntity findByBookId(Long id);
}
