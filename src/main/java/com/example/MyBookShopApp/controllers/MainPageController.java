package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainPageController {

    private final BookService bookService;

    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> getRecommendedBooks() {
        return bookService.getBooksData();
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> getRecentBooks() {
        return bookService.getBooksData();
    }

    @ModelAttribute("popularBooks")
    public List<BookEntity> getPopularBooks() {
        return bookService.getBooksData();
    }

    @ModelAttribute("popularBooks")
    public List<BookEntity> getPopularBook() {
        return bookService.getBooksData();
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> getRecentBook() {
        return bookService.getBooksData();
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/popular")
    public String getPopularBooksPage() {
        return "books/popular";
    }

    @GetMapping("/recent")
    public String getRecentBooksPage() {
        return "books/recent";
    }

    @GetMapping("/slug")
    public String getBookInfo(@RequestParam(value = "bookTitle", required = false) String bookTitle,
                              @RequestParam(value = "bookAuthorName", required = false) String bookAuthorName,
                              @RequestParam(value = "bookPriceOld", required = false) String bookPriceOld,
                              @RequestParam(value = "bookPrice", required = false) String bookPrice,
                              Model model) {
        model.addAttribute("bookTitle", bookTitle);
        model.addAttribute("bookAuthorName", bookAuthorName);
        model.addAttribute("bookPriceOld", bookPriceOld);
        model.addAttribute("bookPrice", bookPrice);
        return "books/slug";
    }

    @GetMapping("/postponed")
    public String getPostponedBooks() {
        return "postponed";
    }

    @GetMapping("/cart")
    public String getCart() {
        return "cart";
    }

    @GetMapping("/signin")
    public String getSignInPage() {
        return "signin";
    }

    @GetMapping("/about")
    public String getAboutPage() {
        return "about";
    }

    @GetMapping("/contacts")
    public String getContactsPage() {
        return "contacts";
    }

    @GetMapping("/faq")
    public String getFAQPage() {
        return "faq";
    }
}


