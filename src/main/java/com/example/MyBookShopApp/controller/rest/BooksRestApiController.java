package com.example.MyBookShopApp.controller.rest;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(description = "Book data api")
public class BooksRestApiController {

    private final BookService bookService;

    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/by-author")
    @ApiOperation("Getting a list of books by author name")
    public ResponseEntity<List<BookEntity>> booksByAuthor(@RequestParam("author") String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    @GetMapping("/books/by-title")
    @ApiOperation("Getting a list of books by book name")
    public ResponseEntity<List<BookEntity>> booksByTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    @GetMapping("/books/by-price-range")
    @ApiOperation("Getting a list of books by price range")
    public ResponseEntity<List<BookEntity>> getPriceRangeBooks(@RequestParam("min") Integer min,
                                                               @RequestParam("max") Integer max) {
        return ResponseEntity.ok(bookService.getBooksWithPriceBetween(min, max));
    }

    @GetMapping("/books/with-max-discount")
    @ApiOperation("Getting a list of books with the maximum discount")
    public ResponseEntity<List<BookEntity>> getMaxDiscountBooks() {
        return ResponseEntity.ok(bookService.getBooksWithMaxDiscount());
    }

    @GetMapping("/books/bestsellers")
    @ApiOperation("Getting a list of bestsellers (with is_bestseller = 1)")
    public ResponseEntity<List<BookEntity>> getBestsellers() {
        return ResponseEntity.ok(bookService.getBestsellers());
    }
}
