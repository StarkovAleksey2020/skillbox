package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookShopCart {

    @ModelAttribute(name = "bookCart")
    public List<BookEntity> bookCart() {
        return new ArrayList<>();
    }

    private final BookRepository bookRepository;
    private final BookService bookService;

    @Autowired
    public BookShopCart(BookRepository bookRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(name = "cartContents", required = false) String cartContents,
                                    Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
            String[] cookieSlugs = cartContents.split("/");
            List<BookEntity> booksFromCookieSlugs = bookRepository.findBookEntityBySlugIn(cookieSlugs);
            model.addAttribute("bookCart", booksFromCookieSlugs);
        }
        return "cart";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug,
                                                  @CookieValue(name = "cartContents", required = false) String cartContents,
                                                  HttpServletResponse response,
                                                  Model model) {
        bookService.removeCartContentsItemFromCart(slug, cartContents, response, model);
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/cart/kept/{slug}")
    public String handleMoveBookFromCartToKeptRequest(@PathVariable("slug") String slug,
                                                      @CookieValue(name = "cartContents", required = false) String cartContents,
                                                      @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                                      HttpServletResponse response,
                                                      Model model) {
        bookService.removeCartContentsItemFromCart(slug, cartContents, response, model);
        bookService.addPostponedContentsItem(slug, postponedContents, response, model);
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         @CookieValue(name = "cartContents", required = false) String cartContents,
                                         HttpServletResponse response,
                                         Model model) {
        bookService.addCartContentsItem(slug, cartContents, response, model);
        return "redirect:/books/" + slug;
    }
}
