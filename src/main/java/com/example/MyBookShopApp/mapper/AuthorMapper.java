package com.example.MyBookShopApp.mapper;

import com.example.MyBookShopApp.entity.Author;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class AuthorMapper implements RowMapper<Author> {

    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setAuthor(resultSet.getString("author"));
        return author;
    }
}
