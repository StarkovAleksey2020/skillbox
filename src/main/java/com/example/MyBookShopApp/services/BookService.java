package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private JdbcTemplate jdbcTemplate;
    private AuthorRepository authorRepository;

    @Autowired
    public BookService(JdbcTemplate jdbcTemplate, AuthorRepository authorRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorRepository = authorRepository;
    }

    public List<Book> getBooksData() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {

            Author author = authorRepository.findByIdForShure(rs.getLong("id"));

            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setAuthorName(author.getAuthor());
            book.setTitle(rs.getString("title"));
            book.setPriceOld(rs.getString("priceOld"));
            book.setPrice(rs.getString("price"));
            return book;
        });
        return new ArrayList<>(books);
    }
}
