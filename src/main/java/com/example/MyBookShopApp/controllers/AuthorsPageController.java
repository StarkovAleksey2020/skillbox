package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.services.AuthorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@Api(description = "authors data")
public class AuthorsPageController {

    private AuthorService authorService;

    @Autowired
    public AuthorsPageController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        return "authors/index";
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<AuthorEntity>> getAuthorsMap() {
        return authorService.createAuthorsHashMap();
    }

    @GetMapping("/authors/slug")
    public String slugPage(@RequestParam(value = "authorName", required = false) String authorName,
                           Model model) {
        model.addAttribute("authorName", authorName);
        return "authors/slug";
    }

    @ApiOperation("method to get authors page")
    @GetMapping("/api/authors")
    @ResponseBody
    public Map<String, List<AuthorEntity>> getAuthorsHashMap() {
        return authorService.createAuthorsHashMap();
    }

}
