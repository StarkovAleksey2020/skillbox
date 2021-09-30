package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.security.UserEntityDetails;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookShopCart {

    @ModelAttribute(name = "bookCart")
    public List<BookEntity> bookCart() {
        return new ArrayList<>();
    }

    @ModelAttribute("postponedSize")
    public Integer getPostponedSize(@CookieValue(name = "postponedContents", required = false) String postponedContents) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                return bookService.getPostponedCount();
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
                return bookService.getCartCount();
            }
        } catch (Exception e) {
            return bookService.getCartCountTempUser(cartContents);
        }
        return 0;
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
        if (bookService.getCartCount() == 0) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("bookCart", bookService.getBookListInCart());
        }
        return "cart";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug,
                                                  Model model) {
        bookService.removeCartItem(slug);
        model.addAttribute("postponedSize", bookService.getPostponedCount());
        model.addAttribute("cartContentsSize", bookService.getCartCount());
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/cart/kept/{slug}")
    public String handleMoveBookFromCartToKeptRequest(@PathVariable("slug") String slug,
                                                      Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookService.addPostponedItem(slug, principal);
        model.addAttribute("postponedSize", bookService.getPostponedCount());
        model.addAttribute("cartContentsSize", bookService.getCartCount());
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         Model model) {
        bookService.addCartItem(slug);
        model.addAttribute("postponedSize", bookService.getPostponedCount());
        model.addAttribute("cartContentsSize", bookService.getCartCount());
        return "redirect:/books/" + slug;
    }
}
