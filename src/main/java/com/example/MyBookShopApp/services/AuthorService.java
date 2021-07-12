package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.repository.JDBC.JdbcAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private JdbcAuthorRepository authorRepository;

    @Autowired
    public AuthorService(JdbcAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAuthorsData() {
        List<Author> authors = authorRepository.findAll();
        return authors;
    }

    public Map<String, List<Author>> createAuthorsHashMap() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().collect(Collectors.groupingBy((Author a) -> {
            return a.getAuthor().substring(0, 1);
        }));
    }
}
