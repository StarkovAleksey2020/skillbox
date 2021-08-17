package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Api(description = "tagged data")
public class TagsPageController {

    private final BookService bookService;

    public TagsPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("taggedBooks")
    public List<BookEntity> getTaggedBooks() {
        return new ArrayList<>();
    }

    @ApiOperation("method to get tagged page")
    @GetMapping("/tags")
    public String getTagsPage(
            @RequestParam("tagName") String tagName,
            @RequestParam("offset") Integer offset,
            @RequestParam("limit") Integer limit,
            Model model) {
        model.addAttribute("tagName", tagName);
        model.addAttribute("taggedBooks", bookService.getPageOfTaggedBooks(tagName, offset, limit).getContent());
        return "tags/index";
    }

}
