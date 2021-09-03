package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.service.BookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        Boolean result = bookService.createBookReview(slug, comment);
        session.setAttribute("isCommentLeft", result);
        return "redirect:/books/" + slug;
    }
}
