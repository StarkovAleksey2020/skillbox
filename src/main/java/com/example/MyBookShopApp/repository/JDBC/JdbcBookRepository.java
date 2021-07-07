package com.example.MyBookShopApp.repository.JDBC;

import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.mapper.BookMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private JdbcAuthorRepository authorRepository;


    public JdbcBookRepository(NamedParameterJdbcTemplate jdbcTemplate, JdbcAuthorRepository authorRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = jdbcTemplate.query(
                "select * from books",
                new BookMapper(authorRepository));
        return new ArrayList<>(books);
    }
}
