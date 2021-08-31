package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.links.Book2TagEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final Book2TagRepository book2TagRepository;

    private Integer DEFAULT_OFFSET = 0;
    private Integer DEFAULT_LIMIT = 10;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, TagRepository tagRepository, Book2TagRepository book2TagRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.book2TagRepository = book2TagRepository;
    }

    @Autowired

    public List<BookEntity> getBooksData() {
        return bookRepository.findAll();
    }

    public List<BookEntity> getBooksByAuthor(String authorName) {
        AuthorEntity entity = authorRepository.findAuthorEntityByName(authorName);
        return bookRepository.findBookEntitiesByAuthorSetContaining(entity);
    }


    public List<BookEntity> getBooksByTitle(String title) throws BookstoreAPiWrongParameterException {
        if (title.equals("") || title.length() <= 1) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<BookEntity> bookEntities = bookRepository.findBookEntitiesByTitleContaining(title);
            if (bookEntities.size() > 0 ) {
                return bookEntities;
            } else {
                throw new BookstoreAPiWrongParameterException("No data found with specified parameters...");
            }
        }
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
        Pageable nextPage0 = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);

        OffsetDateTime dateTimeFrom = getDateOffset(dateFrom);
        OffsetDateTime dateTimeTo = getDateOffset(dateTo);

        return bookRepository.findBookEntitiesByPubDateAfterAndPubDateBefore(dateTimeFrom, dateTimeTo, nextPage0);
    }

    private OffsetDateTime getDateOffset(String inputString) {
        SimpleDateFormat fromUser = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        String reformattedStr = null;
        try {
            reformattedStr = myFormat.format(fromUser.parse(inputString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return OffsetDateTime.parse(reformattedStr + "T01:00:00+03:00");
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
            nextPage = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);
        }
        return bookRepository.findBooksByGenreName(genreName, nextPage);
    }

    public Page<BookEntity> getPageOfBooksByAuthorName(String authorName, Integer offset, Integer limit) {
        Pageable nextPage;
        if (offset!=null || limit!=null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);
        }
        return bookRepository.findBooksByAuthor(authorName, nextPage);
    }

    public Page<BookEntity> getPageOfBooksByFolderId(Long folderId, Integer offset, Integer limit) {
        Pageable nextPage;
        if (offset!=null || limit!=null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);
        }
        return bookRepository.findBooksByFolder(folderId, nextPage);
    }

    public Integer getAuthorBooksCount(String authorName) {
        return bookRepository.findBooksCountByAuthor(authorName);
    }

}
