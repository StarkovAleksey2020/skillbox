package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthorsPageController {

    private AuthorService authorService;

    @Autowired
    public AuthorsPageController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        model.addAttribute("authorsData", authorService.getAuthorsData());
        return "authors";
    }

    @GetMapping("/authors/slug")
    public String slugPage() { return "slug_authors"; }

}
