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

    @ModelAttribute("postponedSize")
    public Integer getPostponedSize() {
        return bookService.getPostponedCount();
    }

    @ModelAttribute("cartContentsSize")
    public Integer getCartContentsSize() {
        return bookService.getCartCount();
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
        bookService.addPostponedItem(slug);
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
