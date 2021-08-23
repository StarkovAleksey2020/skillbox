package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.links.Book2TagEntity;
import com.example.MyBookShopApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BookService {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private TagRepository tagRepository;
    private Book2TagRepository book2TagRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, TagRepository tagRepository, Book2TagRepository book2TagRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.book2TagRepository = book2TagRepository;
    }

    public List<BookEntity> getBooksData() {
        return bookRepository.findAll();
    }

    public List<BookEntity> getBooksByAuthor(String authorName) {
        AuthorEntity entity = authorRepository.findAuthorEntityByName(authorName);
        return bookRepository.findBookEntitiesByAuthorSetContaining(entity);
    }


    public List<BookEntity> getBooksByTitle(String title) {
        return bookRepository.findBookEntitiesByTitleContaining(title);
    }

    public List<BookEntity> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBookEntitiesByPriceBetween(min, max);
    }

    public List<BookEntity> getBooksWithPrice(Integer price) {
        return bookRepository.findBookEntitiesByPriceIs(price);
    }

    public List<BookEntity> getBooksWithMaxDiscount() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<BookEntity> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public Page<BookEntity> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAllDesc(nextPage);
    }

    public Page<BookEntity> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookEntitiesByTitleContaining(searchWord, nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooksInterval(String dateFrom, String dateTo, Integer offset, Integer limit) {
        Pageable nextPage0 = PageRequest.of(0, 10);

        OffsetDateTime dateTimeFrom = getDateOffset(dateFrom);
        OffsetDateTime dateTimeTo = getDateOffset(dateTo);

        return bookRepository.findBookEntitiesByPubDateAfterAndPubDateBefore(dateTimeFrom, dateTimeTo, nextPage0);
    }

    private OffsetDateTime getDateOffset(String inputString) {
        return OffsetDateTime.parse(inputString.substring(6, 10) + "-" + inputString.substring(3, 5) + "-" + inputString.substring(0, 2) + "T01:00:00+03:00");
    }

    public Page<BookEntity> getPageOfPopularBooksOrdered(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPopularRate(nextPage);
    }

    public Page<BookEntity> getPageOfTaggedBooks(String tagName, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTagName(tagName, nextPage);
    }

    public String getBookTag(Long bookId) {
        Book2TagEntity book2TagEntity = book2TagRepository.findByBookId(bookId);
        return tagRepository.findById(book2TagEntity.getTagId()).get().getName();
    }

    public Page<BookEntity> getPageOfBooksByGenreName(String genreName, Integer offset, Integer limit) {
        Pageable nextPage;
        if (offset!=null || limit!=null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(0, 10);
        }
        return bookRepository.findBooksByGenreName(genreName, nextPage);
    }

    public Page<BookEntity> getPageOfBooksByAuthorName(String authorName, Integer offset, Integer limit) {
        Pageable nextPage;
        if (offset!=null || limit!=null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(0, 10);
        }
        return bookRepository.findBooksByAuthor(authorName, nextPage);
    }

    public Page<BookEntity> getPageOfBooksByFolderId(Long folderId, Integer offset, Integer limit) {
        Pageable nextPage;
        if (offset!=null || limit!=null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(0, 10);
        }
        return bookRepository.findBooksByFolder(folderId, nextPage);
    }

    public Integer getAuthorBooksCount(String authorName) {
        return bookRepository.findBooksCountByAuthor(authorName);
    }

}
