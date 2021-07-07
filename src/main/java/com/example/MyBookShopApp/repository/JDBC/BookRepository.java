package com.example.MyBookShopApp.repository.JDBC;

import com.example.MyBookShopApp.entity.Book;

import java.util.List;

public interface BookRepository {

    List<Book> findAll();
}
