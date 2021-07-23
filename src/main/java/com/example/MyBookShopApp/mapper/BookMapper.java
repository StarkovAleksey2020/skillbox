package com.example.MyBookShopApp.mapper;

import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.repository.JDBC.JdbcAuthorRepository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    private JdbcAuthorRepository authorRepository;

    public BookMapper(JdbcAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        String authorName = authorRepository.findById(resultSet.getLong("author_id")).getAuthor();

        Book book = new Book();
        book.setId(resultSet.getLong("id"));
//        book.setAuthor(resultSet.getLong("author"));
        book.setAuthorName(authorName);
        book.setTitle(resultSet.getString("title"));
        book.setPriceOld(resultSet.getString("price_old"));
        book.setPrice(resultSet.getString("price"));

        return book;
    }
}
