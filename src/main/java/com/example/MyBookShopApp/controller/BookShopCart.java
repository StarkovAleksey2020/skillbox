package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.security.UserEntityDetails;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookShopCart {

    private final BookRepository bookRepository;
    private final BookService bookService;

    @Autowired
    public BookShopCart(BookRepository bookRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @ModelAttribute(name = "bookCart")
    public List<BookEntity> bookCart() {
        return new ArrayList<>();
    }

    @ModelAttribute("postponedSize")
    public Integer getPostponedSize(@CookieValue(name = "postponedContents", required = false) String postponedContents) {
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

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(name = "cartContents", required = false) String cartContents,
                                    Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                if (bookService.getCartCount(principal) == 0) {
                    model.addAttribute("isCartEmpty", true);
                } else {
                    model.addAttribute("isCartEmpty", false);
                    model.addAttribute("bookCart", bookService.getBookListInCart(principal));
                }
            }
        } catch (Exception e) {
            if (bookService.getCartCountTempUser(cartContents) == 0) {
                model.addAttribute("isCartEmpty", true);
            } else {
                model.addAttribute("isCartEmpty", false);
                model.addAttribute("bookCart", bookService.getBookListInCartUserTemp(cartContents));
            }
        }
        return "cart";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug,
                                                  @CookieValue(name = "cartContents", required = false) String cartContents,
                                                  @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                                  HttpServletResponse response,
                                                  Model model) throws BookstoreAPiWrongParameterException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                bookService.removeCartItem(slug, principal);
                model.addAttribute("postponedSize", bookService.getPostponedCount(principal));
                model.addAttribute("cartContentsSize", bookService.getCartCount(principal));
                model.addAttribute("bookCart", bookService.getBookListInCart(principal));
            }
        } catch (Exception e) {
            Cookie cookie = bookService.removeCartItemTempUser(slug, cartContents);
            response.addCookie(cookie);
            model.addAttribute("cartSize", bookService.getCartCountTempUser(cartContents));
            model.addAttribute("isCartEmpty", bookService.getCartCountTempUser(cartContents) == 0);
            model.addAttribute("bookCart", bookService.getBookListInCartUserTemp(cartContents));
        }
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/cart/kept/{slug}")
    public String handleMoveBookFromCartToKeptRequest(@PathVariable("slug") String slug,
                                                      @CookieValue(name = "cartContents", required = false) String cartContents,
                                                      @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                                      HttpServletResponse response,
                                                      Model model) throws BookstoreAPiWrongParameterException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                bookService.addPostponedItem(slug, principal);
                model.addAttribute("postponedSize", bookService.getPostponedCount(principal));
                model.addAttribute("cartContentsSize", bookService.getCartCount(principal));
                model.addAttribute("bookCart", bookService.getBookListInCart(principal));
            }
        } catch (Exception e) {
            Cookie cookie = bookService.addPostponedItemTempUser(slug, postponedContents);
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);

            Cookie cookieCart = bookService.removeCartItemTempUser(slug, cartContents);
            response.addCookie(cookieCart);

            model.addAttribute("postponedSize", bookService.getPostponedCountTempUser(postponedContents));
            model.addAttribute("cartContentsSize", bookService.getCartCountTempUser(cartContents));
            model.addAttribute("bookCart", bookService.getBookListInCartUserTemp(cartContents));
        }
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         @CookieValue(name = "cartContents", required = false) String cartContents,
                                         @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                         HttpServletResponse response,
                                         Model model) throws BookstoreAPiWrongParameterException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                bookService.addCartItem(slug, principal);
                model.addAttribute("postponedSize", bookService.getPostponedCount(principal));
                model.addAttribute("cartContentsSize", bookService.getCartCount(principal));
            }
        } catch (Exception e) {
            Cookie cookie = bookService.addCartItemTempUser(slug, cartContents);
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);

            model.addAttribute("postponedSize", bookService.getPostponedCountTempUser(postponedContents));
            model.addAttribute("cartContentsSize", bookService.getCartCountTempUser(cartContents));
        }

        return "redirect:/books/" + slug;
    }
}
