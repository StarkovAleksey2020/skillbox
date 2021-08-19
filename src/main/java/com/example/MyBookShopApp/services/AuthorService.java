package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

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

    public String getDescriptionVisible(String description) {
        String[] strings = description.split("\\.");
        return strings[0] + ". " + strings[1] + ".";
    }

    public String getDescriptionHidden(String description) {
        String[] strings = description.split("\\.");
        String descriptionHidden = "";
        for (int i = 2; i < strings.length; i++) {
            descriptionHidden += strings[i] + ". ";
        }
        return descriptionHidden;
    }

}
