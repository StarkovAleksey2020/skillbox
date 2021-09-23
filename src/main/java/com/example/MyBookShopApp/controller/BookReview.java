package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.user.UserEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.security.UserEntityDetails;
import com.example.MyBookShopApp.service.BookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class BookReview {

    private final BookService bookService;

    @Autowired
    public BookReview(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/bookReview/{slug}/{comment}")
    @ApiOperation("Submitting book reviews")
    public String postComment(@PathVariable("slug") String slug,
                              @PathVariable("comment") String comment,
                              HttpSession session) throws BookstoreAPiWrongParameterException {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!principal.equals("anonymousUser")) {

            try {
                UserEntity userEntity = ((UserEntityDetails) principal).getUserEntity();
                Boolean result = bookService.createBookReview(slug, comment, userEntity);
                if (result) {
                    session.setAttribute("isCommentLeft", 0);
                } else {
                    session.setAttribute("isCommentLeft", 3);
                }
            } catch (Exception e) {
                session.setAttribute("isCommentLeft", 2);
            }

        } else if (principal.equals("anonymousUser")) {
            session.setAttribute("isCommentLeft", 1);
        }
        return "redirect:/books/" + slug;
    }
}
