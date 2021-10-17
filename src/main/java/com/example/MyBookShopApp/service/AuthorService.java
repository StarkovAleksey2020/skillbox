package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.aop.annotations.NullArgsCatchable;
import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    public List<AuthorEntity> getAuthorsData() {
        return authorRepository.findAll();
    }

    public Map<String, List<AuthorEntity>> createAuthorsHashMap() {
        List<AuthorEntity> authors = authorRepository.findAll();
        return authors.stream().collect(Collectors.groupingBy((AuthorEntity a) -> {
            return a.getName().substring(0, 1);
        }));
    }

    @NullArgsCatchable
    public String getDescriptionVisible(String description) throws BookstoreAPiWrongParameterException {
            String[] strings = description.split("\\.");
            return strings[0] + ". " + strings[1] + ".";
    }

    @NullArgsCatchable
    public String getDescriptionHidden(String description) throws BookstoreAPiWrongParameterException {
            String[] strings = description.split("\\.");
            StringBuilder descriptionHidden = new StringBuilder();
            for (int i = 2; i < strings.length; i++) {
                descriptionHidden.append(strings[i]).append(". ");
            }
            return descriptionHidden.toString();
    }

}
