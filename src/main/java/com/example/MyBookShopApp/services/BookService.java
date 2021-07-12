package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.repository.JDBC.JdbcBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private JdbcBookRepository jdbcBookRepository;

    @Autowired
    public BookService(JdbcBookRepository jdbcBookRepository) {
        this.jdbcBookRepository = jdbcBookRepository;
    }

    public List<Book> getBooksData() {
        return jdbcBookRepository.findAll();
    }

}
