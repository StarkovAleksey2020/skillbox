package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookShopPostponed {

    @ModelAttribute(name = "bookPostponed")
    public List<BookEntity> bookPostponed() {
        return new ArrayList<>();
    }

    @ModelAttribute("postponedSize")
    public Integer getPostponedSize() {
        return bookService.getPostponedCount();
    }

    @ModelAttribute("cartContentsSize")
    public Integer getCartContentsSize() {
        return bookService.getCartCount();
    }

    private final BookService bookService;

    public BookShopPostponed(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/postponed")
    public String handlePostponedRequest(@CookieValue(name = "postponedContents", required = false) String postponedContents,
                                         Model model) {
        if (bookService.getPostponedCount() == 0) {
            model.addAttribute("isPostponedEmpty", true);
        } else {
            model.addAttribute("isPostponedEmpty", false);
            model.addAttribute("bookPostponed", bookService.getBookListInPostponed());
        }
        return "postponed";
    }

    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String handleRemoveBookFromPostponedRequest(@PathVariable("slug") String slug,
                                                       Model model) {
        bookService.removePostponedItem(slug);
        model.addAttribute("postponedSize", bookService.getPostponedCount());
        model.addAttribute("cartContentsSize", bookService.getCartCount());
        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/postponed/buy/{slug}")
    public String handleBuyBookFromPostponedRequest(@PathVariable("slug") String slug,
                                                    Model model) {
        bookService.addCartItem(slug);
        model.addAttribute("postponedSize", bookService.getPostponedCount());
        model.addAttribute("cartContentsSize", bookService.getCartCount());
        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/postponed/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         Model model) {
        bookService.addPostponedItem(slug);
        model.addAttribute("postponedSize", bookService.getPostponedCount());
        model.addAttribute("cartContentsSize", bookService.getCartCount());
        return "redirect:/books/" + slug;
    }
}
