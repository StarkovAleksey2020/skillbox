package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    GenreEntity findByName(String name);
}
