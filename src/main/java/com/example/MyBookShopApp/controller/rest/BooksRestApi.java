package com.example.MyBookShopApp.controller.rest;

import com.example.MyBookShopApp.data.ApiResponse;
import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.other.LinkEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(description = "Book data api")
public class BooksRestApi {

    private final BookService bookService;

    @Autowired
    public BooksRestApi(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/by-author")
    @ApiOperation("Getting a list of books by author name")
    public ResponseEntity<List<BookEntity>> booksByAuthor(@RequestParam("author") String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    @GetMapping("/books/by-title")
    @ApiOperation("Getting a list of books by book name")
    public ResponseEntity<ApiResponse<BookEntity>> booksByTitle(@RequestParam("title") String title) throws BookstoreAPiWrongParameterException {
        ApiResponse<BookEntity> response = new ApiResponse<>();
        List<BookEntity> data = bookService.getBooksByTitle(title);
        response.setDebugMessage("successful request");
        response.setMessage("data size: " + data.size() + " elements");
        response.setHttpStatus(HttpStatus.OK);
        response.setTimeStamp(LocalDateTime.now());
        response.setData(data);

        List<LinkEntity> entities = new ArrayList<>();
        LinkEntity entity = new LinkEntity();
        entity.setId(1L);
        entity.setUrl("https://www.litmir.me/");
        entities.add(entity);

        response.setLinks(entities);
        return ResponseEntity.ok(response);
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

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<BookEntity>> handleMissingServletRequestParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<BookEntity>(HttpStatus.BAD_REQUEST, "Missing required parameters", exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BookstoreAPiWrongParameterException.class)
    public ResponseEntity<ApiResponse<BookEntity>> handleBookstoreAPiWrongParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<BookEntity>(HttpStatus.BAD_REQUEST, "Bad parameter value...", exception), HttpStatus.BAD_REQUEST);
    }

}
