package com.example.MyBookShopApp.controller.rest;

import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.exception.EmptySearchException;
import com.example.MyBookShopApp.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Api(description = "Pagination data api")
public class BookPaginationRestApi {

    private final BookService bookService;

    public BookPaginationRestApi(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/recommended")
    @ApiOperation("Getting a page-by-page list of recommended books")
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/books/recent")
    @ApiOperation("Getting a page-by-page list of recent books")
    public BooksPageDto getBooksRecentPage(@RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecentBooks(offset, limit).getContent());
    }

    @GetMapping("/books/tags")
    @ApiOperation("Getting a page-by-page list of tagged books")
    public BooksPageDto getBooksTagsPage(@RequestParam("tagName") String tagName,
                                         @RequestParam("offset") Integer offset,
                                         @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfTaggedBooks(tagName, offset, limit).getContent());
    }

    @GetMapping("/books/genre")
    @ApiOperation("Getting a paginated list of books by genre")
    public BooksPageDto getBooksPageByGenre(@RequestParam("genreName") String genreName,
                                            @RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfBooksByGenreName(genreName, offset, limit).getContent());
    }

    @GetMapping("/books/author")
    @ApiOperation("Getting a paginated list of books by author id")
    public BooksPageDto getBooksPageByAuthor(@RequestParam("authorName") String authorName,
                                             @RequestParam("offset") Integer offset,
                                             @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfBooksByAuthorName(authorName, offset, limit).getContent());
    }

    @GetMapping("/books/folder/genre")
    @ApiOperation("Getting a page-by-page list of books by genre sections")
    public BooksPageDto getBooksPageByGenreFolder(@RequestParam("folderId") Long folderId,
                                                  @RequestParam("offset") Integer offset,
                                                  @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfBooksByFolderId(folderId, offset, limit).getContent());
    }

    @GetMapping("/books/recent/interval")
    @ApiOperation("method to get recent books in date interval")
    public BooksPageDto getBooksRecentPageInDateInterval(@RequestParam("from") String from,
                                                         @RequestParam("to") String to,
                                                         @RequestParam("offset") Integer offset,
                                                         @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecentBooksInterval(from, to, offset, limit).getContent());
    }

    @GetMapping("/books/popular")
    @ApiOperation("method to get popular books ordered by book2user")
    public BooksPageDto getBooksPopularPage(@RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooksOrdered(offset, limit).getContent());
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    @ApiOperation("method to search books")
    public ModelAndView getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                         Model model) throws EmptySearchException {
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResults",
                    bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 5).getContent());
            return new ModelAndView("search/index");
        } else {
            throw new EmptySearchException("Поиск по NULL невозможен");
        }

    }

    @GetMapping("/search/page/{searchWord}")
    @ApiOperation("Getting a page-by-page list of books by search")
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit).getContent());
    }

}
