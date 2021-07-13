package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class MainPageController {

    private final BookService bookService;

    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recommendedBooks")
    public List<Book> getRecommendedBooks() {
        return bookService.getBooksData();
    }

    @ModelAttribute("popularBooks")
    public List<Book> getPopularBook() {
        return bookService.getBooksData();
    }

    @ModelAttribute("recentBooks")
    public List<Book> getRecentBook() {
        return bookService.getBooksData();
    }

    @GetMapping("/")
    public String mainPage() {
        return "/index";
    }

    @GetMapping("/popular")
    public String getPopularBooksPage() {
        return "/books/popular";
    }

    @GetMapping("/recent")
    public String getRecentBooksPage() {
        return "/books/recent";
    }

    @GetMapping("/slug")
    public String getBookInfo() {
        return "/books/slug";
    }

    @GetMapping("/postponed")
    public String getPostponedBooks() {
        return "/postponed";
    }
}

