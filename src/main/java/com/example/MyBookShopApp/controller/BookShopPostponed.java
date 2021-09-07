package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/books")
public class BookShopPostponed {

    @ModelAttribute(name = "bookPostponed")
    public List<BookEntity> bookPostponed() {
        return new ArrayList<>();
    }

    @ModelAttribute(name = "postponedSize")
    public Integer postponedSize() {
        return 0;
    }

    private final BookRepository bookRepository;
    private final BookService bookService;

    @Autowired
    public BookShopPostponed(BookRepository bookRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @GetMapping("/postponed")
    public String handlePostponedRequest(@CookieValue(name = "postponedContents", required = false) String postponedContents,
                                         Model model) {
        if (postponedContents == null || postponedContents.equals("")) {
            model.addAttribute("isPostponedEmpty", true);
        } else {
            model.addAttribute("isPostponedEmpty", false);
            postponedContents = postponedContents.startsWith("/") ? postponedContents.substring(1) : postponedContents;
            postponedContents = postponedContents.endsWith("/") ? postponedContents.substring(0, postponedContents.length() - 1) : postponedContents;
            String[] cookieSlugs = postponedContents.split("/");
            List<BookEntity> booksFromCookieSlugs = bookRepository.findBookEntityBySlugIn(cookieSlugs);
            model.addAttribute("bookPostponed", booksFromCookieSlugs);
        }
        return "postponed";
    }

    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String handleRemoveBookFromPostponedRequest(@PathVariable("slug") String slug,
                                                       @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                                       HttpServletResponse response,
                                                       Model model) {
        bookService.removePostponedContentsItemFromPostponed(slug, postponedContents, response, model);
        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/postponed/buy/{slug}")
    public String handleBuyBookFromPostponedRequest(@PathVariable("slug") String slug,
                                                    @CookieValue(name = "cartContents", required = false) String cartContents,
                                                    @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                                    HttpServletResponse response,
                                                    Model model) {
        bookService.removePostponedContentsItemFromPostponed(slug, postponedContents, response, model);
        bookService.addCartContentsItem(slug, cartContents, response, model);
        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/postponed/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                         HttpServletResponse response,
                                         Model model) {
        bookService.addPostponedContentsItem(slug, postponedContents, response, model);
        return "redirect:/books/" + slug;
    }

    @GetMapping("/postponed/count")
    public ModelAndView getPostponedCount(@CookieValue(name = "cartContents", required = false) String cartContents,
                                          @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                          Model model) {
        if (cartContents != null) {
            model.addAttribute("cartContentsSize", bookService.getCartCount(cartContents));
        } else {
            model.addAttribute("cartContentsSize", 0);
        }
        if (postponedContents != null) {
            model.addAttribute("postponedSize", bookService.getPostponedCount(postponedContents));
        } else {
            model.addAttribute("postponedSize", 0);
        }
        return new ModelAndView("/fragments/header_fragment");
    }
}
