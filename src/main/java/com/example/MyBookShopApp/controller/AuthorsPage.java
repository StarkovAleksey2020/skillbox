package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.service.AuthorService;
import com.example.MyBookShopApp.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@Api(description = "authors data")
public class AuthorsPage {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorsPage(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
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
    @ApiOperation("Getting information for the author's page")
    public String slugPage(@RequestParam(value = "authorName", required = false) String authorName,
                           @RequestParam(value = "description", required = false) String description,
                           @RequestParam(value = "photo", required = false) String photo,
                           @RequestParam(value = "authorId", required = false) Long authorId,
                           Model model) {
        model.addAttribute("authorName", authorName);
        model.addAttribute("descriptionVisible", authorService.getDescriptionVisible(description));
        model.addAttribute("descriptionHidden", authorService.getDescriptionHidden(description));
        model.addAttribute("photo", photo);
        model.addAttribute("authorBooks", new BooksPageDto(bookService.getPageOfBooksByAuthorName(authorName, 0, 10).getContent()));
        model.addAttribute("authorId", authorId);
        model.addAttribute("authorTotalBooks", bookService.getAuthorBooksCount(authorName));
        return "authors/slug";
    }
}
