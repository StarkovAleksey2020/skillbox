package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.links.Book2TagEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.Book2TagRepository;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.repository.TagRepository;
import com.example.MyBookShopApp.utils.NullableConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final Book2TagRepository book2TagRepository;
    private final TagRepository tagRepository;

    private Integer DEFAULT_LIMIT = 10;

//    @MockBean
//    private BookRepository bookRepositoryMock;

    @Autowired
    BookServiceTest(BookService bookService, BookRepository bookRepository, Book2TagRepository book2TagRepository, TagRepository tagRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.book2TagRepository = book2TagRepository;
        this.tagRepository = tagRepository;
    }

    @BeforeEach
    void setUp() {
//        BookEntity bookEntity = new BookEntity();
//        bookEntity.setImage("/some/image/path");
//        bookEntity.setTitle("Test Title");
//        bookEntity.setDescription("Test Book Description");
//        bookRepositoryMock.save(bookEntity);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void getBooksByTitle_withEmptyInputParams_getBookstoreAPiWrongParameterException(String title) {
        Exception exception = assertThrows(BookstoreAPiWrongParameterException.class, () -> {
            bookService.getBooksByTitle(title);
        });
        assertEquals("Wrong values passed to one or more parameters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"111", "SomeWrongTitle"})
    void getBooksByTitle_withWrongInputParams_getBookstoreAPiWrongParameterException(String title) {
        Exception exception = assertThrows(BookstoreAPiWrongParameterException.class, () -> {
            bookService.getBooksByTitle(title);
        });
        assertEquals("No data found with specified parameters...", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Batman", "Randy and the Mob"})
    void getBooksByTitle_withCorrectInputParams_getSingleSizeArray(String title) throws BookstoreAPiWrongParameterException {
        final int EXPECTED_LENGTH = 1;
        List<BookEntity> bookEntities = bookService.getBooksByTitle(title);
        assertNotNull(bookEntities);
        assertEquals(bookEntities.size(), EXPECTED_LENGTH);
    }

    @ParameterizedTest
    @ValueSource(strings = {"man"})
    void getBooksByTitle_withCorrectInputParams_getMultipleSizeArray(String title) throws BookstoreAPiWrongParameterException {
        List<BookEntity> bookEntities = bookService.getBooksByTitle(title);
        assertNotNull(bookEntities);
        assertTrue(bookEntities.size() > 1);
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void getBooksByAuthor_withEmptyInputParams_getBookstoreAPiWrongParameterException(String authorName) {
        Exception exception = assertThrows(BookstoreAPiWrongParameterException.class, () -> {
            bookService.getBooksByAuthor(authorName);
        });
        assertEquals("Wrong values passed to one or more parameters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"111", "SomeWrongAuthor"})
    void getBooksByAuthor_withWrongInputParams_getBookstoreAPiWrongParameterException(String authorName) {
        Exception exception = assertThrows(BookstoreAPiWrongParameterException.class, () -> {
            bookService.getBooksByAuthor(authorName);
        });
        assertEquals("Wrong values passed to one or more parameters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Tatum Gerb"})
    void getBooksByAuthor_withCorrectInputParams_getAuthorBooksList(String authorName) throws BookstoreAPiWrongParameterException {
        List<BookEntity> bookEntities = bookService.getBooksByAuthor(authorName);
        assertNotNull(bookEntities);
        assertTrue(bookEntities.size() > 0);
    }

    @ParameterizedTest
    @CsvSource({"null, 20000", "10, null", "null, null"})
    void getBooksWithPriceBetween_withNullInputParams_BookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) Integer min,
                                                                                          @ConvertWith(NullableConverter.class) Integer max) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBooksWithPriceBetween(min, max));
    }


    @ParameterizedTest
    @CsvSource({"10, 3000"})
    void getBooksWithPriceBetween_withCorrectInputParams_getBooksList(Integer min, Integer max) throws BookstoreAPiWrongParameterException {
        List<BookEntity> bookEntities = bookService.getBooksWithPriceBetween(min, max);
        assertNotNull(bookEntities);
        assertTrue(bookEntities.size() > 0);
    }


    @Test
    void getBooksWithMaxDiscount() {
        List<BookEntity> bookEntities = bookService.getBooksWithMaxDiscount();
        assertNotNull(bookEntities);
        assertTrue(bookEntities.size() > 0);
    }

    @Test
    void getBestsellers() {
        List<BookEntity> bookEntities = bookService.getBestsellers();
        assertNotNull(bookEntities);
        assertTrue(bookEntities.size() > 0);
    }

    @ParameterizedTest
    @CsvSource({"null, 20000", "10, null", "null, null"})
    void getPageOfRecommendedBooks_withNullInputParams_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) Integer offset,
                                                                                              @ConvertWith(NullableConverter.class) Integer limit) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getPageOfRecommendedBooks(offset, limit));
    }

    @ParameterizedTest
    @CsvSource({"0, 20", "1, 30", "5, 40"})
    void getPageOfRecommendedBooks_withCorrectInputParams_getPageable(Integer offset, Integer limit) {
        assertNotNull(PageRequest.of(offset, limit));
        assertTrue(PageRequest.of(offset, limit) instanceof Pageable);
    }

    @ParameterizedTest
    @CsvSource({"0, 20", "1, 30", "2, 10"})
    void getPageOfRecommendedBooks_withCorrectInputParams_getPage(Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals(limit, bookService.getPageOfRecommendedBooks(offset, limit).getSize());
    }

    @ParameterizedTest
    @CsvSource({"null, 20000", "10, null", "null, null"})
    void getPageOfRecentBooks_withNullInputParams_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) Integer offset,
                                                                                         @ConvertWith(NullableConverter.class) Integer limit) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getPageOfRecentBooks(offset, limit));
    }

    @ParameterizedTest
    @CsvSource({"0, 20", "1, 30", "5, 40"})
    void getPageOfRecentBooks_withCorrectInputParams_getPageable(Integer offset, Integer limit) {
        assertNotNull(PageRequest.of(offset, limit));
        assertTrue(PageRequest.of(offset, limit) instanceof Pageable);
    }

    @ParameterizedTest
    @CsvSource({"0, 20", "1, 30", "2, 10"})
    void getPageOfRecentBooks_withCorrectInputParams_getPage(Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals(limit, bookService.getPageOfRecentBooks(offset, limit).getSize());
    }

    @ParameterizedTest
    @CsvSource({"null, 2, 20", "Batman, null, 10", "null, null, null"})
    void getPageOfSearchResultBooks_withNullInputParams_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String searchWord,
                                                                                               @ConvertWith(NullableConverter.class) Integer offset,
                                                                                               @ConvertWith(NullableConverter.class) Integer limit) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getPageOfSearchResultBooks(searchWord, offset, limit));
    }

    @ParameterizedTest
    @CsvSource({"Batman, 0, 20", "Batman, 1, 30", "Batman, 5, 40"})
    void getPageOfSearchResultBooks_withCorrectInputParams_getPageable(String searchWord, Integer offset, Integer limit) {
        assertNotNull(PageRequest.of(offset, limit));
        assertTrue(PageRequest.of(offset, limit) instanceof Pageable);
    }

    @ParameterizedTest
    @CsvSource({"Batman, 0, 20", "Batman, 0, 30", "Batman, 0, 10"})
    void getPageOfSearchResultBooks_withCorrectInputParams_getPage(String searchWord, Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals(limit, bookService.getPageOfSearchResultBooks(searchWord, offset, limit).getSize());
        assertTrue(bookService.getPageOfSearchResultBooks(searchWord, offset, limit).getContent().get(0).getTitle().equals("Batman"));
    }

    @ParameterizedTest
    @CsvSource({"01.01.1970, null, 0, 10", "null, 31.12.2021, 0, 10", "null, null, null, null"})
    void getPageOfRecentBooksInterval_withNullInputParams_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String dateFrom,
                                                                                                 @ConvertWith(NullableConverter.class) String dateTo,
                                                                                                 @ConvertWith(NullableConverter.class) Integer offset,
                                                                                                 @ConvertWith(NullableConverter.class) Integer limit) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getPageOfRecentBooksInterval(dateFrom, dateTo, offset, limit));
    }

    @ParameterizedTest
    @CsvSource({"01.01.1970, 31.12.2021, 0, 10", "01.01.1970, 31.12.2021, 0, 10"})
    void getPageOfRecentBooksInterval_withCorrectInputParams_getPageable(String dateFrom, String dateTo, Integer offset, Integer limit) {
        assertNotNull(PageRequest.of(offset, limit));
        assertTrue(PageRequest.of(offset, limit) instanceof Pageable);
    }

    @ParameterizedTest
    @CsvSource({"01.01.1970, 31.12.2021, 0, 10"})
    void getPageOfRecentBooksInterval_withCorrectInputParams_getPage(String dateFrom, String dateTo, Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        assertNotNull(bookService.getPageOfRecentBooksInterval(dateFrom, dateTo, offset, limit));
        assertEquals(limit, bookService.getPageOfRecentBooksInterval(dateFrom, dateTo, offset, limit).getSize());
    }

    @ParameterizedTest
    @CsvSource({"01.01.2122, 31.12.3021, 0, 10"})
    void getPageOfRecentBooksInterval_withDistantInTimeInputParams_getNull(String dateFrom, String dateTo, Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        assertNotNull(bookService.getPageOfRecentBooksInterval(dateFrom, dateTo, offset, limit));
        assertEquals(limit, bookService.getPageOfRecentBooksInterval(dateFrom, dateTo, offset, limit).getSize());
        assertEquals(0, bookService.getPageOfRecentBooksInterval(dateFrom, dateTo, offset, limit).getContent().size());
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void getDateOffset_withNullInputParams_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String inputString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getDateOffset(inputString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1970/12/01"})
    void getDateOffset_withWrongInputParams_getBookstoreAPiWrongParameterException(String inputString) {
        Exception exception = assertThrows(BookstoreAPiWrongParameterException.class, () -> {
            bookService.getDateOffset(inputString);
        });
        assertEquals("Invalid date format", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"01.12.1970"})
    void getDateOffset_withCorrectInputParams_getCorrectOffsetDateTime(String inputString) throws BookstoreAPiWrongParameterException {
        final String EXPECTED_DATETIME = "1970-12-01T01:00+03:00";
        assertNotNull(bookService.getDateOffset(inputString));
        assertEquals(EXPECTED_DATETIME, bookService.getDateOffset(inputString).toString());
        assertTrue(bookService.getDateOffset(inputString) instanceof OffsetDateTime);
    }

    @ParameterizedTest
    @CsvSource({"null, 20000", "10, null", "null, null"})
    void getPageOfPopularBooksOrdered_withNullInputParams_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) Integer offset,
                                                                                                 @ConvertWith(NullableConverter.class) Integer limit) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getPageOfPopularBooksOrdered(offset, limit));
    }

    @ParameterizedTest
    @CsvSource({"0, 20", "1, 30", "5, 40"})
    void getPageOfPopularBooksOrdered_withCorrectInputParams_getPageable(Integer offset, Integer limit) {
        assertNotNull(PageRequest.of(offset, limit));
        assertTrue(PageRequest.of(offset, limit) instanceof Pageable);
    }

    @ParameterizedTest
    @CsvSource({"0, 20", "1, 30", "2, 10"})
    void getPageOfPopularBooksOrdered_withCorrectInputParams_getPage(Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals(limit, bookService.getPageOfPopularBooksOrdered(offset, limit).getSize());
    }

    @ParameterizedTest
    @CsvSource({"современная литература, null, 20000", "null, 10, null", "null, null, null"})
    void getPageOfTaggedBooks_withNullInputParams_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String tagName,
                                                                                         @ConvertWith(NullableConverter.class) Integer offset,
                                                                                         @ConvertWith(NullableConverter.class) Integer limit) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getPageOfTaggedBooks(tagName, offset, limit));
    }

    @ParameterizedTest
    @CsvSource({"0, 20", "1, 30", "5, 40"})
    void getPageOfTaggedBooks_withCorrectInputParams_getPageable(Integer offset, Integer limit) {
        assertNotNull(PageRequest.of(offset, limit));
        assertTrue(PageRequest.of(offset, limit) instanceof Pageable);
    }

    @ParameterizedTest
    @CsvSource({"современная литература, 0, 20", "современная литература, 1, 30", "современная литература, 2, 10"})
    void getPageOfTaggedBooks_withCorrectInputParams_getPage(String tagName, Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals(limit, bookService.getPageOfTaggedBooks(tagName, offset, limit).getSize());
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void getBookTag_withNullInputParams_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) Long bookId) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookTag(bookId));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void getBookTag_withCorrectInputParams_getBook2TagEntity(Long bookId) throws BookstoreAPiWrongParameterException {
        Book2TagRepository book2TagRepositoryMock = Mockito.mock(Book2TagRepository.class);
        doAnswer(new Answer<Book2TagEntity>() {
            @Override
            public Book2TagEntity answer(InvocationOnMock invocationOnMock) throws Throwable {
                Book2TagEntity book2TagEntity = new Book2TagEntity();
                book2TagEntity.setBookId(1L);
                book2TagEntity.setTagId(10L);
                return book2TagEntity;
            }
        }).when(book2TagRepositoryMock).findByBookId(anyLong());

        ArgumentCaptor<Long> arg = ArgumentCaptor.forClass(Long.class);

        Book2TagEntity entity = book2TagRepositoryMock.findByBookId(bookId);
        Mockito.verify(book2TagRepositoryMock, times(1)).findByBookId(arg.capture());
        assertEquals(1L, arg.getValue());
        assertEquals(10L, (long) entity.getTagId());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void getBookTag_withCorrectInputParams_getTagName(Long bookId) throws BookstoreAPiWrongParameterException {
        assertEquals("современная литература", bookService.getBookTag(bookId));
    }

    @ParameterizedTest
    @CsvSource({"null, 0, 10"})
    void getPageOfBooksByGenreName_withNullGenreNameInputParams_getException(@ConvertWith(NullableConverter.class) String genreName,
                                                                             @ConvertWith(NullableConverter.class) Integer offset,
                                                                             @ConvertWith(NullableConverter.class) Integer limit) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getPageOfBooksByGenreName(genreName, offset, limit));
    }

    @ParameterizedTest
    @CsvSource({"Фантастика, null, null"})
    void getPageOfBooksByGenreName_withNullPageParamsInputParams_getDefaultPageParams(@ConvertWith(NullableConverter.class) String genreName,
                                                                                      @ConvertWith(NullableConverter.class) Integer offset,
                                                                                      @ConvertWith(NullableConverter.class) Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals(DEFAULT_LIMIT, bookService.getPageOfBooksByGenreName(genreName, offset, limit).getSize());
    }

    @ParameterizedTest
    @CsvSource({"Фантастика, 0, 10"})
    void getPageOfBooksByGenreName_withCorrectPageParamsInputParams_getDefaultPageParams(@ConvertWith(NullableConverter.class) String genreName,
                                                                                         @ConvertWith(NullableConverter.class) Integer offset,
                                                                                         @ConvertWith(NullableConverter.class) Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals("Randy and the Mob", bookService.getPageOfBooksByGenreName(genreName, offset, limit).getContent().get(0).getTitle());
    }

    @ParameterizedTest
    @CsvSource({"null, 0, 10"})
    void getPageOfBooksByAuthorName_withNullAuthorNameInputParams_getException(@ConvertWith(NullableConverter.class) String authorName,
                                                                              @ConvertWith(NullableConverter.class) Integer offset,
                                                                              @ConvertWith(NullableConverter.class) Integer limit) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getPageOfBooksByAuthorName(authorName, offset, limit));
    }

    @ParameterizedTest
    @CsvSource({"Tatum Gerb, null, null"})
    void getPageOfBooksByAuthorName_withNullPageParamsInputParams_getDefaultPageParams(@ConvertWith(NullableConverter.class) String authorName,
                                                                                       @ConvertWith(NullableConverter.class) Integer offset,
                                                                                       @ConvertWith(NullableConverter.class) Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals(DEFAULT_LIMIT, bookService.getPageOfBooksByAuthorName(authorName, offset, limit).getSize());
    }

    @ParameterizedTest
    @CsvSource({"Tatum Gerb, 0, 10"})
    void getPageOfBooksByAuthorName_withCorrectPageParamsInputParams_getDefaultPageParams(@ConvertWith(NullableConverter.class) String authorName,
                                                                                          @ConvertWith(NullableConverter.class) Integer offset,
                                                                                          @ConvertWith(NullableConverter.class) Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals("Randy and the Mob", bookService.getPageOfBooksByAuthorName(authorName, offset, limit).getContent().get(0).getTitle());
    }

    @ParameterizedTest
    @CsvSource({"null, 0, 10"})
    void getPageOfBooksByFolderId_withNullGenreNameInputParams_getException(@ConvertWith(NullableConverter.class) Long folderId,
                                                                              @ConvertWith(NullableConverter.class) Integer offset,
                                                                              @ConvertWith(NullableConverter.class) Integer limit) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getPageOfBooksByFolderId(folderId, offset, limit));
    }

    @ParameterizedTest
    @CsvSource({"1, null, null"})
    void getPageOfBooksByFolderId_withNullPageParamsInputParams_getDefaultPageParams(@ConvertWith(NullableConverter.class) Long folderId,
                                                                                       @ConvertWith(NullableConverter.class) Integer offset,
                                                                                       @ConvertWith(NullableConverter.class) Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals(DEFAULT_LIMIT, bookService.getPageOfBooksByFolderId(folderId, offset, limit).getSize());
    }

    @ParameterizedTest
    @CsvSource({"1, 0, 10"})
    void getPageOfBooksByFolderId_withCorrectPageParamsInputParams_getDefaultPageParams(@ConvertWith(NullableConverter.class) Long folderId,
                                                                                          @ConvertWith(NullableConverter.class) Integer offset,
                                                                                          @ConvertWith(NullableConverter.class) Integer limit) throws BookstoreAPiWrongParameterException {
        assertEquals("Randy and the Mob", bookService.getPageOfBooksByFolderId(folderId, offset, limit).getContent().get(0).getTitle());
    }


//    @Test
//    void getPageOfBooksByFolderId() {
//    }
//
//    @Test
//    void getAuthorBooksCount() {
//    }
//
//    @Test
//    void addCartContentsItem() {
//    }
}