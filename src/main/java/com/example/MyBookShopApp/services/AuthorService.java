package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Author> getAuthorsData() {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors", (ResultSet rs, int rowNum) -> {

            Author author = new Author();
            author.setId(rs.getLong("id"));
            author.setAuthor(rs.getString("author"));
            return author;
        });
        return new ArrayList<>(authors);

    }
}
