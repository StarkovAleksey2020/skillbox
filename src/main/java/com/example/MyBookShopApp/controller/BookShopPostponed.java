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

    private final BookService bookService;

    public BookShopPostponed(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/postponed")
    public String handlePostponedRequest(@CookieValue(name = "postponedContents", required = false) String postponedContents,
                                         Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                if (bookService.getPostponedCount(principal) == 0) {
                    model.addAttribute("isPostponedEmpty", true);
                } else {
                    model.addAttribute("isPostponedEmpty", false);
                    model.addAttribute("bookPostponed", bookService.getBookListInPostponed(principal));
                    model.addAttribute("postponedSize", bookService.getPostponedCount(principal));
                    model.addAttribute("cartContentsSize", bookService.getCartCount(principal));
                }
            }
        } catch (Exception e) {
            if (bookService.getPostponedCountTempUser(postponedContents) == 0) {
                model.addAttribute("isPostponedEmpty", true);
            } else {
                model.addAttribute("isPostponedEmpty", false);
                model.addAttribute("bookPostponed", bookService.getBookListInPostponedUserTemp(postponedContents));
            }
        }
        return "postponed";
    }

    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String handleRemoveBookFromPostponedRequest(@PathVariable("slug") String slug,
                                                       @CookieValue(name = "cartContents", required = false) String cartContents,
                                                       @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                                       HttpServletResponse response,
                                                       Model model) throws BookstoreAPiWrongParameterException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                bookService.removePostponedItem(slug, principal);
                model.addAttribute("postponedSize", bookService.getPostponedCount(principal));
                model.addAttribute("cartContentsSize", bookService.getCartCount(principal));
                model.addAttribute("bookPostponed", bookService.getBookListInPostponed(principal));
            }
        } catch (Exception e) {
            Cookie cookie = bookService.removePostponedItemTempUser(slug, postponedContents);
            response.addCookie(cookie);
            model.addAttribute("postponedSize", bookService.getPostponedCountTempUser(postponedContents));
            model.addAttribute("isPostponedEmpty", bookService.getPostponedCountTempUser(postponedContents) == 0);
            model.addAttribute("bookPostponed", bookService.getBookListInPostponedUserTemp(postponedContents));
        }
        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/postponed/buy/{slug}")
    public String handleBuyBookFromPostponedRequest(@PathVariable("slug") String slug,
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
                model.addAttribute("bookPostponed", bookService.getBookListInPostponed(principal));
            }
        } catch (Exception e) {
            Cookie cookie = bookService.addCartItemTempUser(slug, cartContents);
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);

            Cookie cookiePostponed = bookService.removePostponedItemTempUser(slug, postponedContents);
            response.addCookie(cookiePostponed);

            model.addAttribute("postponedSize", bookService.getPostponedCountTempUser(postponedContents));
            model.addAttribute("cartContentsSize", bookService.getCartCountTempUser(cartContents));
            model.addAttribute("bookPostponed", bookService.getBookListInPostponedUserTemp(postponedContents));
        }
        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/postponed/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
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
