package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    @Query(value = "SELECT u FROM AuthorEntity u WHERE u.id = :id")
    AuthorEntity findByIdExactly(@Param("id") Long id);
}
