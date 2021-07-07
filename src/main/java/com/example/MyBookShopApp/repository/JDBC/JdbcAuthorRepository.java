package com.example.MyBookShopApp.repository.JDBC;

import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.mapper.AuthorMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcAuthorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author findById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.queryForObject(
                "select * from authors where id=:id", params, new AuthorMapper());
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = jdbcTemplate.query(
                "select * from authors", new AuthorMapper());
        return new ArrayList<>(authors);
    }

}
