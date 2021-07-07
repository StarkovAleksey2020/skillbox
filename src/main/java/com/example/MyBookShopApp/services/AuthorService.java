package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.repository.JDBC.JdbcAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private JdbcAuthorRepository authorRepository;

    @Autowired
    public AuthorService(JdbcAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAuthorsData() {
        return authorRepository.findAll();
    }

//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public AuthorService(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

//    public List<Author> getAuthorsData() {
//        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors", (ResultSet rs, int rowNum) -> {
//
//            Author author = new Author();
//            author.setId(rs.getLong("id"));
//            author.setAuthor(rs.getString("author"));
//            return author;
//        });
//        return new ArrayList<>(authors);
//    }
}
