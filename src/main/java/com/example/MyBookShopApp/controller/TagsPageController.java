package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.service.BookService;
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

    @GetMapping("/tags/slug")
    public String getBookInfo(@RequestParam(value = "bookTitle", required = false) String bookTitle,
                              @RequestParam(value = "bookAuthorName", required = false) String bookAuthorName,
                              @RequestParam(value = "description", required = false) String description,
                              @RequestParam(value = "id", required = false) Long id,
                              @RequestParam(value = "price", required = false) String price,
                              @RequestParam(value = "bookImage", required = false) String bookImage,
                              @RequestParam(value = "isBestseller", required = false) Integer isBestseller,
                              @RequestParam(value = "bookDiscount", required = false) String bookDiscount,
                              Model model) {
        model.addAttribute("bookTitle", bookTitle);
        model.addAttribute("bookAuthorName", bookAuthorName);
        model.addAttribute("description", description);
        model.addAttribute("tag", bookService.getBookTag(id));
        model.addAttribute("price", price);
        model.addAttribute("bookImage", bookImage);
        model.addAttribute("isBestseller", isBestseller);
        model.addAttribute("bookDiscount", bookDiscount);
        return "books/slug";
    }

}
