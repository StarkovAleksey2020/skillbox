package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
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
public class BookShopPostponed {

    @ModelAttribute(name = "bookPostponed")
    public List<BookEntity> bookPostponed() {
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
                                                       Model model) throws BookstoreAPiWrongParameterException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookService.removePostponedItem(slug, principal);
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
                                         @CookieValue(name = "cartContents", required = false) String cartContents,
                                         @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                         HttpServletResponse response,
                                         Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                bookService.addPostponedItem(slug, principal);
                model.addAttribute("postponedSize", bookService.getPostponedCount());
                model.addAttribute("cartContentsSize", bookService.getCartCount());
            }
        } catch (Exception e) {
            Cookie cookie = bookService.addPostponedItemTempUser(slug, postponedContents);
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);

            model.addAttribute("postponedSize", bookService.getPostponedCountTempUser(postponedContents));
            model.addAttribute("cartContentsSize", bookService.getCartCountTempUser(cartContents));
        }

        return "redirect:/books/" + slug;
    }
}
