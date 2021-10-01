package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.security.UserEntityDetails;
import com.example.MyBookShopApp.service.BookService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Api(description = "main controller")
public class MainPage {

    private final BookService bookService;

    private Integer DEFAULT_OFFSET = 0;
    private Integer DEFAULT_LIMIT = 10;

    @Autowired
    public MainPage(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> getRecommendedBooks() throws BookstoreAPiWrongParameterException {
        return bookService.getPageOfRecommendedBooks(DEFAULT_OFFSET, DEFAULT_LIMIT).getContent();
    }

    @ModelAttribute("postponedSize")
    public Integer getPostponedSize(@CookieValue(name = "postponedContents", required = false) String postponedContents,
                                    Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                return bookService.getPostponedCount(principal);
            }
        } catch (Exception e) {
            return bookService.getPostponedCountTempUser(postponedContents);
        }
        return 0;
    }

    @ModelAttribute("cartContentsSize")
    public Integer getCartContentsSize(@CookieValue(name = "cartContents", required = false) String cartContents) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                return bookService.getCartCount(principal);
            }
        } catch (Exception e) {
            return bookService.getCartCountTempUser(cartContents);
        }
        return 0;
    }

    @ModelAttribute("popularBooks")
    public List<BookEntity> getPopularBooks() throws BookstoreAPiWrongParameterException {
        return bookService.getPageOfPopularBooksOrdered(DEFAULT_OFFSET, DEFAULT_LIMIT).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> getRecentBook() throws BookstoreAPiWrongParameterException {
        return bookService.getPageOfRecentBooks(DEFAULT_OFFSET, DEFAULT_LIMIT).getContent();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResults")
    public List<BookEntity> searchResults() {
        return new ArrayList<>();
    }

    @ModelAttribute("recentBooksResults")
    public List<BookEntity> getRecentBooksResults() {
        return new ArrayList<>();
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


