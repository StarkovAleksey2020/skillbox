package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.repository.JDBC.JdbcAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
public class AuthorService {

    private JdbcAuthorRepository authorRepository;

    @Autowired
    public AuthorService(JdbcAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAuthorsData() {
        List<Author> authors = authorRepository.findAll();

        HashMap<String, List<Author>> listHashMap = createAuthorsHashMap(authors);

        return authors;
    }

    private HashMap<String, List<Author>> createAuthorsHashMap(List<Author> authors) {

        HashMap<String, List<Author>> hashMap = new HashMap<>();

        for (int counter = 0; counter < authors.size(); counter++) {

            String firstSymbol = authors.get(counter).getAuthor().toUpperCase(Locale.ROOT).substring(0, 1);
            Author author = authors.get(counter);

            if (!hashMap.containsKey(firstSymbol)) {

                List<Author> list = new ArrayList<>();
                list.add(author);

                hashMap.put(firstSymbol, list);
            } else {

                List<Author> list = hashMap.get(firstSymbol);

                list.add(author);

                hashMap.put(firstSymbol, list);
            }
        }

        return hashMap;
    }
}
