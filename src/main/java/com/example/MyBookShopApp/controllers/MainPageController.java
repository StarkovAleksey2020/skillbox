package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Api(description = "main controller")
public class MainPageController {

    private final BookService bookService;

    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> getRecommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 10).getContent();
    }

    @ModelAttribute("popularBooks")
    public List<BookEntity> getPopularBooks() {
        return bookService.getPageOfPopularBooksOrdered(0, 10).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> getRecentBook() {
        List<BookEntity> bookEntities = bookService.getPageOfRecentBooks(0, 10).getContent();
        return bookEntities;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResults")
    public List<BookEntity> searchResults() {
        return new ArrayList<>();
    }

    @ModelAttribute("recentBooksResults")
    public List<BookEntity> getRecentBooksResults() {
        return new ArrayList<>();
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/popular")
    public String getPopularBooksPage() {
        return "books/popular";
    }

    @GetMapping("/recent")
    public String getRecentBooksPage() {
        return "books/recent";
    }

    @GetMapping("/slug")
    public String getBookInfo(@RequestParam(value = "bookTitle", required = false) String bookTitle,
                              @RequestParam(value = "bookAuthorName", required = false) String bookAuthorName,
                              @RequestParam(value = "bookPriceOld", required = false) String bookPriceOld,
                              @RequestParam(value = "bookPrice", required = false) String bookPrice,
                              Model model) {
        model.addAttribute("bookTitle", bookTitle);
        model.addAttribute("bookAuthorName", bookAuthorName);
        model.addAttribute("bookPriceOld", bookPriceOld);
        model.addAttribute("bookPrice", bookPrice);
        return "books/slug";
    }

    @GetMapping("/postponed")
    public String getPostponedBooks() {
        return "postponed";
    }

    @GetMapping("/cart")
    public String getCart() {
        return "cart";
    }

    @GetMapping("/signin")
    public String getSignInPage() {
        return "signin";
    }

    @GetMapping("/about")
    public String getAboutPage() {
        return "about";
    }

    @GetMapping("/contacts")
    public String getContactsPage() {
        return "contacts";
    }

    @GetMapping("/faq")
    public String getFAQPage() {
        return "faq";
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/books/recent")
    @ResponseBody
    public BooksPageDto getBooksRecentPage(@RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecentBooks(offset, limit).getContent());
    }

    @GetMapping("/books/tags")
    @ResponseBody
    public BooksPageDto getBooksTagsPage(@RequestParam("tagName") String tagName,
                                         @RequestParam("offset") Integer offset,
                                         @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfTaggedBooks(tagName, offset, limit).getContent());
    }

    @GetMapping("/books/genre")
    @ResponseBody
    public BooksPageDto getBooksPageByGenre(@RequestParam("genreId") Long genreId,
                                            @RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfBooksByGenreId(genreId, offset, limit).getContent());
    }

    @GetMapping("/books/recent/interval")
    @ApiOperation("method to get recent books in date interval")
    @ResponseBody
    public BooksPageDto getBooksRecentPageInDateInterval(@RequestParam("from") String from,
                                                         @RequestParam("to") String to,
                                                         @RequestParam("offset") Integer offset,
                                                         @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecentBooksInterval(from, to, offset, limit).getContent());
    }

    @GetMapping("/books/popular")
    @ApiOperation("method to get popular books ordered by book2user")
    @ResponseBody
    public BooksPageDto getBooksPopularPage(@RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooksOrdered(offset, limit).getContent());
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    @ApiOperation("method to search books")
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                   Model model) {
        model.addAttribute("searchWordDto", searchWordDto);
        model.addAttribute("searchResults",
                bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 5).getContent());
        return "/search/index";
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit).getContent());
    }
}


