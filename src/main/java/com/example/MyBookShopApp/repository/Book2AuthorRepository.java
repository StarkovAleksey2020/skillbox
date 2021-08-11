package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.book.links.Book2AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Book2AuthorRepository extends JpaRepository<Book2AuthorEntity, Long> {
    List<Book2AuthorEntity> findByAuthorId(Long id);
}
