package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.book.links.Book2GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2GenreRepository extends JpaRepository<Book2GenreEntity, Long> {

}
