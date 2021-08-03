package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TagsPageController {

    private final BookService bookService;

    public TagsPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("taggedBooks")
    public List<BookEntity> getTaggedBooks() {
        return bookService.getBooksData();
    }

    @GetMapping("/tags")
    public String getTagsPage(
            @RequestParam(value = "tagName", required = false) String tagName,
            Model model) {
        model.addAttribute("tagName", tagName);
        return "tags/index";
    }

}
