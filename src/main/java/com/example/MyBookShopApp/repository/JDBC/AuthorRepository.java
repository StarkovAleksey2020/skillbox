package com.example.MyBookShopApp.repository.JDBC;

import com.example.MyBookShopApp.entity.Author;

import java.util.List;

public interface AuthorRepository {
    Author findById(long id);

    List<Author> findAll();
}
