package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.links.Book2TagEntity;
import com.example.MyBookShopApp.entity.genre.GenreEntity;
import com.example.MyBookShopApp.entity.tag.TagEntity;
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
    private Book2AuthorRepository book2AuthorRepository;
    private AuthorRepository authorRepository;
    private TagRepository tagRepository;
    private Book2TagRepository book2TagRepository;
    private GenreRepository genreRepository;

    @Autowired
    public BookService(BookRepository bookRepository, Book2AuthorRepository book2AuthorRepository, AuthorRepository authorRepository, TagRepository tagRepository, Book2TagRepository book2TagRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.book2AuthorRepository = book2AuthorRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.book2TagRepository = book2TagRepository;
        this.genreRepository = genreRepository;
    }

    public List<BookEntity> getBooksData() {
        return bookRepository.findAll();
    }

    // NEW

    public List<BookEntity> getBooksByAuthor(String authorName) {
        AuthorEntity entity = authorRepository.findAuthorEntityByName(authorName);
        return bookRepository.findBookEntitiesByAuthorSetContaining(entity);
    }


    public List<BookEntity> getBooksByTitle(String title) {
        List<BookEntity> bookEntities = bookRepository.findBookEntitiesByTitleContaining(title);
//        return bookRepository.findBookEntitiesByTitleContaining(title);
        return bookEntities;
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

    public Page<BookEntity> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookEntitiesByTitleContaining(searchWord, nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooksInterval(String dateFrom, String dateTo, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Pageable nextPage0 = PageRequest.of(0, 10);

        OffsetDateTime dateTimeFrom = getDateOffset(dateFrom);
        OffsetDateTime dateTimeTo = getDateOffset(dateTo);

        Page<BookEntity> bookEntities = bookRepository.findBookEntitiesByPubDateAfterAndPubDateBefore(dateTimeFrom, dateTimeTo, nextPage0);
        return bookEntities;
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
        TagEntity tagEntity = tagRepository.findByName(tagName);
        return bookRepository.findBooksByTag(tagEntity.getId(), nextPage);
    }

    public String getBookTag(Long bookId) {
        Book2TagEntity book2TagEntity = book2TagRepository.findByBookId(bookId);
        return tagRepository.findById(book2TagEntity.getTagId()).get().getName();
    }

    public Page<BookEntity> getPageOfBooksByGenre(String genreName, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        GenreEntity genreEntity = genreRepository.findByName(genreName);
        return bookRepository.findBooksByGenre(genreEntity.getId(), nextPage);
    }

    public Page<BookEntity> getPageOfBooksByGenreId(Long genreId, Integer offset, Integer limit) {
        Pageable nextPage;
        if (offset!=null || limit!=null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(0, 10);
        }
        return bookRepository.findBooksByGenre(genreId, nextPage);
    }

    public Long getGenreId(String genreName) {
        return genreRepository.getGenreByName(genreName).getId();
    }
}
