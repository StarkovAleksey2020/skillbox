package com.example.MyBookShopApp.controller.rest;

import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.service.AuthorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(description = "Author data api")
public class AuthorsRestApi {

    private final AuthorService authorService;

    public AuthorsRestApi(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ApiOperation("method to get authors page")
    @GetMapping("/authors")
    @ResponseBody
    public Map<String, List<AuthorEntity>> getAuthorsHashMap() {
        return authorService.createAuthorsHashMap();
    }

}
