package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
}
