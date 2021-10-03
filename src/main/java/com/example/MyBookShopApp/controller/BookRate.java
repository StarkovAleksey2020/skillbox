package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.service.BookService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookRate {

    private final BookService bookService;

    public BookRate(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/rateBook/{slug}/{rate}")
    public String handleBookRate(@PathVariable("slug") String slug,
                                 @PathVariable("rate") Integer rate,
                                 Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("bookRate", bookService.setBookRate(slug, rate, principal));
        return "redirect:/books/" + slug;
    }

}
