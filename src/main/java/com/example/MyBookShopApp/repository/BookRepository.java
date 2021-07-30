package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
