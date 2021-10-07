package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.links.Book2RateEntity;
import com.example.MyBookShopApp.entity.book.links.Book2TagEntity;
import com.example.MyBookShopApp.entity.cart.CartEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.*;
import com.example.MyBookShopApp.security.UserEntityDetails;
import com.example.MyBookShopApp.security.UserEntityRepository;
import com.example.MyBookShopApp.utils.NullableConverter;
import com.example.MyBookShopApp.utils.TestUtil;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final Book2TagRepository book2TagRepository;
    private final TagRepository tagRepository;
    private UserEntityRepository userEntityRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private BookRateRepository bookRateRepository;

    private Integer DEFAULT_LIMIT = 10;

    private UserEntity TEST_USER_ENTITY = new UserEntity();
    private UserEntity TEST_USER_ENTITY_ADMIN = new UserEntity();
    private CartEntity TEST_CART_ENTITY = new CartEntity();
    private List<Book2RateEntity> TEST_BOOK_RATE_ENTITY_LIST = new ArrayList<>();

    private Object principal;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private CartRepository cartRepositoryMock;

    @MockBean
    private UserRepository userRepositoryMock;

    @MockBean
    private BookRateRepository bookRateRepositoryMock;

    @Autowired
    BookServiceTest(BookService bookService, BookRepository bookRepository, Book2TagRepository book2TagRepository, TagRepository tagRepository, UserEntityRepository userEntityRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, BookRateRepository bookRateRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.book2TagRepository = book2TagRepository;
        this.tagRepository = tagRepository;
        this.userEntityRepository = userEntityRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.bookRateRepository = bookRateRepository;
    }

    @BeforeEach
    void setUp() {
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        principal = getPrincipal();

        TEST_USER_ENTITY.setId(1L);
        TEST_USER_ENTITY.setEmail("test9@example.com");

        TEST_USER_ENTITY_ADMIN.setId(2L);
        TEST_USER_ENTITY_ADMIN.setEmail("admin@example.com");

        TEST_CART_ENTITY.setUserEntity(TEST_USER_ENTITY);
        TEST_CART_ENTITY.setEditDate(OffsetDateTime.now());
        TEST_CART_ENTITY.setValue("{\"cartString\":\"\",\"postponedString\":\"\"}");

        Book2RateEntity book2RateEntity = new Book2RateEntity();
        book2RateEntity.setBookEntity(mock(BookEntity.class));
        book2RateEntity.setUserEntity(mock(UserEntity.class));
        book2RateEntity.setRate(3);
        TEST_BOOK_RATE_ENTITY_LIST.add(book2RateEntity);

    }

    @AfterEach
    void tearDown() {
        UserEntity userEntity = userEntityRepository.findUserEntityByEmail("test9@example.com");
        if (userEntity != null) {
            userEntityRepository.delete(userEntity);
        }
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

    @ParameterizedTest
    @CsvSource({"null"})
    void getAuthorBooksCount_withNullAuthorNameInputParams_getException(@ConvertWith(NullableConverter.class) String authorName) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getAuthorBooksCount(authorName));
    }

    @ParameterizedTest
    @CsvSource({"Tatum Gerb"})
    void getAuthorBooksCount_withCorrectPageParamsInputParams_getAuthorBooksCount(String authorName) throws BookstoreAPiWrongParameterException {
        assertEquals(11, bookService.getAuthorBooksCount(authorName));
    }

    @ParameterizedTest
    @CsvSource({"null, null"})
    void removePostponedItem_withNullSlugInputParams_getException(@ConvertWith(NullableConverter.class) String slug,
                                                                  @ConvertWith(NullableConverter.class) Object principal) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removePostponedItem(slug, principal));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"cartString\":\"\",\"postponedString\":\"/lNv3IiN\"}"})
    void getCartItemEntity_withCorrectPostponedValue_getCartItemEntityWithPostponed(String valueString) {
        assertEquals("/lNv3IiN", bookService.getCartItemEntity(valueString).getPostponedString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"cartString\":\"/lNv3IiN\",\"postponedString\":\"\"}"})
    void getCartItemEntity_withCorrectPostponedValue_getCartItemEntityWithCart(String valueString) {
        assertEquals("/lNv3IiN", bookService.getCartItemEntity(valueString).getCartString());
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void getCartItemEntity_withCorrectPostponedValue_getNewCartItemEntity(String valueString) {
        assertNull(bookService.getCartItemEntity(valueString).getCartString());
        assertNull(bookService.getCartItemEntity(valueString).getPostponedString());
    }


    private void createTestUser(String email, String password) {
        userEntityRepository.save(TestUtil.createUser(email, password, bCryptPasswordEncoder, userEntityRepository));
    }

    @Test
    void getCartCountTempUser_withNull_getZero() {
        assertEquals(0, bookService.getCartCountTempUser(null));
    }

    @Test
    void getCartCountTempUser_withEmpty_getZero() {
        assertEquals(0, bookService.getCartCountTempUser(""));
    }

    @Test
    void getCartCountTempUser_withCorrectvalue_getBooksCount() {
        assertEquals(3, bookService.getCartCountTempUser("aaaa/bbbbb/cccc"));
    }

    @Test
    void getPostponedCountTempUser_withNull_getZero() {
        assertEquals(0, bookService.getPostponedCountTempUser(null));
    }

    @Test
    void getPostponedCountTempUser_withEmpty_getZero() {
        assertEquals(0, bookService.getPostponedCountTempUser(""));
    }

    @Test
    void getPostponedCountTempUser_withCorrectvalue_getBooksCount() {
        assertEquals(3, bookService.getPostponedCountTempUser("aaaa/bbbbb/cccc"));
    }


    @Test
    void getBookListInCartUserTemp_withNull_getEmptyArray() {
        assertEquals(0, bookService.getBookListInCartUserTemp(null).size());
    }

    @Test
    void getBookListInCartUserTemp_withEmpty_getEmptyArray() {
        assertEquals(0, bookService.getBookListInCartUserTemp("").size());
    }

    @Test
    void getBookListInCartUserTemp_withCorrectValue_getNotEmptyArray() {
        assertEquals(2, bookService.getBookListInCartUserTemp("lNv3IiN/QJFBcrET").size());
    }

    @Test
    void getBookListInPostponedUserTemp_withNull_getEmptyArray() {
        assertEquals(0, bookService.getBookListInPostponedUserTemp(null).size());
    }

    @Test
    void getBookListInPostponedUserTemp_withEmpty_getEmptyArray() {
        assertEquals(0, bookService.getBookListInPostponedUserTemp("").size());
    }

    @Test
    void getBookListInPostponedUserTemp_withCorrectValue_getNotEmptyArray() {
        assertEquals(2, bookService.getBookListInPostponedUserTemp("lNv3IiN/QJFBcrET").size());
    }

    @ParameterizedTest
    @CsvSource({"null, test_comment"})
    void createBookReview_withInvalidSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                 @ConvertWith(NullableConverter.class) String comment) {
        UserEntity userEntity = userRepository.findByName("Davine Fassan");
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.createBookReview(slug, comment, userEntity));
    }

    @ParameterizedTest
    @CsvSource({"QJFBcrET, null"})
    void createBookReview_withInvalidComment_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                    @ConvertWith(NullableConverter.class) String comment) {
        UserEntity userEntity = userRepository.findByName("Davine Fassan");
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.createBookReview(slug, comment, userEntity));
    }

    @ParameterizedTest
    @CsvSource({"null, null"})
    void createBookReview_withInvalidSlugAndComment_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                           @ConvertWith(NullableConverter.class) String comment) {
        UserEntity userEntity = userRepository.findByName("Davine Fassan");
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.createBookReview(slug, comment, userEntity));
    }

    @ParameterizedTest
    @CsvSource({"aaaaa"})
    void getBookReviewInfo_withInvalidSlug_getBookstoreAPiWrongParameterException(String slug) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookReviewInfo(slug));
    }

    @ParameterizedTest
    @CsvSource({"erileQ20vp"})
    void getBookReviewInfo_withCorrectSlug_getPositiveBookReviewInfoArraySize(String slug) throws BookstoreAPiWrongParameterException {
        assertEquals(1, bookService.getBookReviewInfo(slug).size());
    }

    @ParameterizedTest
    @CsvSource({"null, 1, 1, 1"})
    void handleReviewLikesDislikes_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                       @ConvertWith(NullableConverter.class) Long reviewId,
                                                                                       @ConvertWith(NullableConverter.class) Long value,
                                                                                       @ConvertWith(NullableConverter.class) Long userId) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.handleReviewLikesDislikes(slug, reviewId, value, userId));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, null, 1, 1"})
    void handleReviewLikesDislikes_withNullReviewId_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                           @ConvertWith(NullableConverter.class) Long reviewId,
                                                                                           @ConvertWith(NullableConverter.class) Long value,
                                                                                           @ConvertWith(NullableConverter.class) Long userId) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.handleReviewLikesDislikes(slug, reviewId, value, userId));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, 1, 1, null"})
    void handleReviewLikesDislikes_withNullUserId_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                         @ConvertWith(NullableConverter.class) Long reviewId,
                                                                                         @ConvertWith(NullableConverter.class) Long value,
                                                                                         @ConvertWith(NullableConverter.class) Long userId) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.handleReviewLikesDislikes(slug, reviewId, value, userId));
    }


    @ParameterizedTest
    @CsvSource({"null"})
    void getBookRateTotal_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRateTotal(slug));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void getBookRateTotal_withEmptySlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRateTotal(slug));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidValue"})
    void getBookRateTotal_withInvalidSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRateTotal(slug));
    }

    @ParameterizedTest
    @ValueSource(strings = {"lNv3IiN"})
    void getBookRateTotal_withValidSlug_getRateCount(String slug) throws BookstoreAPiWrongParameterException {
        assertTrue(bookService.getBookRateTotal(slug) >= 0);
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void getBookRateTotalCount_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRateTotalCount(slug));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void getBookRateTotalCount_withEmptySlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRateTotalCount(slug));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidValue"})
    void getBookRateTotalCount_withInvalidSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRateTotalCount(slug));
    }

    @ParameterizedTest
    @ValueSource(strings = {"lNv3IiN"})
    void getBookRateTotalCount_withValidSlug_getRateCount(String slug) throws BookstoreAPiWrongParameterException {
        assertTrue(bookService.getBookRateTotalCount(slug) >= 0);
    }

    @ParameterizedTest
    @CsvSource({"null, 1"})
    void getBookRateSubTotal_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                 @ConvertWith(NullableConverter.class) int rate) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRateSubTotal(slug, rate));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, -1"})
    void getBookRateSubTotal_withLessZeroRate_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                     @ConvertWith(NullableConverter.class) int rate) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRateSubTotal(slug, rate));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, 2"})
    void getBookRateSubTotal_withValidParams_getBookRateSubTotal(@ConvertWith(NullableConverter.class) String slug,
                                                                 @ConvertWith(NullableConverter.class) int rate) throws BookstoreAPiWrongParameterException {
        assertTrue(bookService.getBookRateSubTotal(slug, rate) >= 0);
    }

    @ParameterizedTest
    @CsvSource({"null, 1"})
    void getBookRateSubTotalCount_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                      @ConvertWith(NullableConverter.class) int rate) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRateSubTotalCount(slug, rate));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, -1"})
    void getBookRateSubTotalCount_withLessZeroRate_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                          @ConvertWith(NullableConverter.class) int rate) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRateSubTotalCount(slug, rate));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, 2"})
    void getBookRateSubTotalCount_withValidParams_getBookRateSubTotal(@ConvertWith(NullableConverter.class) String slug,
                                                                      @ConvertWith(NullableConverter.class) int rate) throws BookstoreAPiWrongParameterException {
        assertTrue(bookService.getBookRateSubTotalCount(slug, rate) >= 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void addPostponedItem_withEmptySlug_getBookstoreAPiWrongParameterException(String slug) {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.addPostponedItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void addPostponedItem_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.addPostponedItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void addPostponedItem_withCorrectSlug_getTrue(String slug) throws BookstoreAPiWrongParameterException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        assertTrue(bookService.addPostponedItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void addPostponedItem_withInvalidPrincipal_getUsernameNotFoundException(String slug) throws UsernameNotFoundException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> bookService.addPostponedItem(slug, principal));
    }

    private Object getPrincipal() {
        UserEntityDetails mockPrincipal = Mockito.mock(UserEntityDetails.class);
        Mockito.when(mockPrincipal.getUsername()).thenReturn("test9@example.com");
        return mockPrincipal;
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void removePostponedItem_withEmptySlug_getBookstoreAPiWrongParameterException(String slug) {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removePostponedItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void removePostponedItem_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removePostponedItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void removePostponedItem_withInvalidPrincipal_getUsernameNotFoundException(String slug) throws UsernameNotFoundException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> bookService.removePostponedItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void removePostponedItem_withCorrectSlugAndHaveInPostponed_getTrue(String slug) throws BookstoreAPiWrongParameterException {
        TEST_CART_ENTITY.setValue("{\"cartString\":\"\",\"postponedString\":\"lNv3IiN\"}");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertTrue(bookService.removePostponedItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void removePostponedItem_withCorrectSlugButHaveNotInPostponed_getFalse(String slug) throws BookstoreAPiWrongParameterException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertFalse(bookService.removePostponedItem(slug, principal));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void removeCartItem_withEmptySlug_getBookstoreAPiWrongParameterException(String slug) {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removeCartItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void removeCartItem_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removeCartItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void removeCartItem_withInvalidPrincipal_getUsernameNotFoundException(String slug) throws UsernameNotFoundException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> bookService.removeCartItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void removeCartItem_withCorrectSlugAndHaveInCart_getTrue(String slug) throws BookstoreAPiWrongParameterException {
        TEST_CART_ENTITY.setValue("{\"cartString\":\"lNv3IiN\",\"postponedString\":\"\"}");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertTrue(bookService.removeCartItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void removeCartItem_withCorrectSlugButHaveNotInCart_getFalse(String slug) throws BookstoreAPiWrongParameterException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertFalse(bookService.removeCartItem(slug, principal));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void addCartItem_withEmptySlug_getBookstoreAPiWrongParameterException(String slug) {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.addCartItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void addCartItem_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.addCartItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void addCartItem_withCorrectSlug_getTrue(String slug) throws BookstoreAPiWrongParameterException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        assertTrue(bookService.addCartItem(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void addCartItem_withInvalidPrincipal_getUsernameNotFoundException(String slug) throws UsernameNotFoundException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> bookService.addCartItem(slug, principal));
    }

    @Test
    void getCartCount_withInvalidPrincipal_getUsernameNotFoundException() throws UsernameNotFoundException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> bookService.getCartCount(principal));
    }

    @Test
    void getCartCount_withValidPrincipalAndNullCartEntity_getZero() {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(null);
        assertEquals(0, bookService.getCartCount(principal));
    }

    @Test
    void getCartCount_withValidPrincipalAndNullEditDateAndNotNullValue_getOne() {
        TEST_CART_ENTITY.setEditDate(null);
        TEST_CART_ENTITY.setValue("{\"cartString\":\"/lNv3IiN\",\"postponedString\":\"\"}");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(1, bookService.getCartCount(principal));
    }

    @Test
    void getCartCount_withValidPrincipalAndNullEditDateAndNullValue_getZero() {
        TEST_CART_ENTITY.setEditDate(null);
        TEST_CART_ENTITY.setValue(null);
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(0, bookService.getCartCount(principal));
    }

    @Test
    void getCartCount_withValidPrincipalAndNullEditDateAndEmptyValue_getZero() {
        TEST_CART_ENTITY.setEditDate(null);
        TEST_CART_ENTITY.setValue("");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(0, bookService.getCartCount(principal));
    }

    @Test
    void getCartCount_withValidPrincipalAndNotNullEditDateAndExpiredEditDate_getZero() {
        TEST_CART_ENTITY.setEditDate(OffsetDateTime.now().minusDays(5L));
        TEST_CART_ENTITY.setValue("");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(0, bookService.getCartCount(principal));
    }

    @Test
    void getCartCount_withValidPrincipalAndNotNullEditDateAndActualEditDate_getFour() {
        TEST_CART_ENTITY.setEditDate(OffsetDateTime.now());
        TEST_CART_ENTITY.setValue("{\"cartString\":\"/aaaa/bbbb/cccc/dddd\",\"postponedString\":\"\"}");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(4, bookService.getCartCount(principal));
    }

    @Test
    void getPostponedCount_withInvalidPrincipal_getUsernameNotFoundException() throws UsernameNotFoundException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> bookService.getPostponedCount(principal));
    }

    @Test
    void getPostponedCount_withValidPrincipalAndNullCartEntity_getZero() {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(null);
        assertEquals(0, bookService.getPostponedCount(principal));
    }

    @Test
    void getPostponedCount_withValidPrincipalAndNullEditDateAndNotNullValue_getOne() {
        TEST_CART_ENTITY.setEditDate(null);
        TEST_CART_ENTITY.setValue("{\"cartString\":\"\",\"postponedString\":\"/lNv3IiN\"}");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(1, bookService.getPostponedCount(principal));
    }

    @Test
    void getPostponedCount_withValidPrincipalAndNullEditDateAndNullValue_getZero() {
        TEST_CART_ENTITY.setEditDate(null);
        TEST_CART_ENTITY.setValue(null);
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(0, bookService.getPostponedCount(principal));
    }

    @Test
    void getPostponedCount_withValidPrincipalAndNullEditDateAndEmptyValue_getZero() {
        TEST_CART_ENTITY.setEditDate(null);
        TEST_CART_ENTITY.setValue("");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(0, bookService.getPostponedCount(principal));
    }

    @Test
    void getPostponedCount_withValidPrincipalAndNotNullEditDateAndExpiredEditDate_getZero() {
        TEST_CART_ENTITY.setEditDate(OffsetDateTime.now().minusDays(5L));
        TEST_CART_ENTITY.setValue("");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(0, bookService.getPostponedCount(principal));
    }

    @Test
    void getPostponedCount_withValidPrincipalAndNotNullEditDateAndActualEditDate_getFour() {
        TEST_CART_ENTITY.setEditDate(OffsetDateTime.now());
        TEST_CART_ENTITY.setValue("{\"cartString\":\"\",\"postponedString\":\"/aaaa/bbbb/cccc/dddd\"}");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(4, bookService.getPostponedCount(principal));
    }

    @Test
    void getBookListInCart_withInvalidPrincipal_getUsernameNotFoundException() throws UsernameNotFoundException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> bookService.getBookListInCart(principal));
    }

    @Test
    void getBookListInCart_withValidPrincipalAndNullCartEntity_getEmptyArray() {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(null);
        assertEquals(0, bookService.getBookListInCart(principal).size());
    }

    @Test
    void getBookListInCart_withValidPrincipalAndNotNullCartEntity_getCartEntity() {
        TEST_CART_ENTITY.setEditDate(OffsetDateTime.now());
        TEST_CART_ENTITY.setValue("{\"cartString\":\"/lNv3IiN/QJFBcrET/fpeP1HkQ/CrAQQf\",\"postponedString\":\"\"}");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(4, bookService.getBookListInCart(principal).size());
    }

    @Test
    void getBookListInPostponed_withInvalidPrincipal_getUsernameNotFoundException() throws UsernameNotFoundException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> bookService.getBookListInPostponed(principal));
    }

    @Test
    void getBookListInPostponed_withValidPrincipalAndNullCartEntity_getEmptyArray() {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(null);
        assertEquals(0, bookService.getBookListInPostponed(principal).size());
    }

    @Test
    void getBookListInPostponed_withValidPrincipalAndNotNullCartEntity_getCartEntity() {
        TEST_CART_ENTITY.setEditDate(OffsetDateTime.now());
        TEST_CART_ENTITY.setValue("{\"cartString\":\"\",\"postponedString\":\"/lNv3IiN/QJFBcrET/fpeP1HkQ/CrAQQf\"}");
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(cartRepositoryMock.findByUserEntity(any())).thenReturn(TEST_CART_ENTITY);
        assertEquals(4, bookService.getBookListInPostponed(principal).size());
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void getBookRate_withEmptySlug_getBookstoreAPiWrongParameterException(String slug) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRate(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void getBookRate_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.getBookRate(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void getBookRate_withNullPrincipal_getZero(String slug) throws BookstoreAPiWrongParameterException {
        assertEquals(0, bookService.getBookRate(slug, null));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void getBookRate_withAnonymousUserPrincipal_getZero(String slug) throws BookstoreAPiWrongParameterException {
        assertEquals(0, bookService.getBookRate(slug, "anonymousUser"));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void getBookRate_withOAuth2Principal_getZero(String slug) throws BookstoreAPiWrongParameterException {
        DefaultOidcUser mockDefaultOidcUser = mock(DefaultOidcUser.class);
        assertEquals(0, bookService.getBookRate(slug, mockDefaultOidcUser));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void getBookRate_withValidPrincipalAndSlug_getThree(String slug) throws BookstoreAPiWrongParameterException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(bookRateRepository.findBook2RateEntityByBookEntityAndUserEntity(any(), any())).thenReturn(TEST_BOOK_RATE_ENTITY_LIST);
        assertEquals(3, bookService.getBookRate(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN"})
    void getBookRate_withValidPrincipalAndSlug_getZeroRate(String slug) throws BookstoreAPiWrongParameterException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(bookRateRepository.findBook2RateEntityByBookEntityAndUserEntity(any(), any())).thenReturn(null);
        assertEquals(0, bookService.getBookRate(slug, principal));
    }

    @ParameterizedTest
    @CsvSource({", 3"})
    void setBookRate_withEmptySlugAndValidRate_getBookstoreAPiWrongParameterException(String slug, Integer rate) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.setBookRate(slug, rate, principal));
    }

    @ParameterizedTest
    @CsvSource({"null, 3"})
    void setBookRate_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                         Integer rate) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.setBookRate(slug, rate, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, 3"})
    void setBookRate_withNullPrincipal_getZero(String slug, Integer rate) throws BookstoreAPiWrongParameterException {
        assertEquals(0, bookService.setBookRate(slug, rate, null));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, 3"})
    void setBookRate_withAnonymousUserPrincipal_getZero(String slug, Integer rate) throws BookstoreAPiWrongParameterException {
        assertEquals(0, bookService.setBookRate(slug, rate, "anonymousUser"));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, 3"})
    void setBookRate_withOAuth2Principal_getZero(String slug, Integer rate) throws BookstoreAPiWrongParameterException {
        DefaultOidcUser mockDefaultOidcUser = mock(DefaultOidcUser.class);
        assertEquals(0, bookService.setBookRate(slug, rate, mockDefaultOidcUser));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, 2"})
    void setBookRate_withValidPrincipalAndSlugAndRate_getNewBookRateAndTwoRate(String slug, Integer rate) throws BookstoreAPiWrongParameterException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(bookRateRepository.findBook2RateEntityByBookEntityAndUserEntity(any(), any())).thenReturn(null);
        assertEquals(2, bookService.setBookRate(slug, rate, principal));
    }

    @ParameterizedTest
    @CsvSource({"lNv3IiN, 4"})
    void setBookRate_withValidPrincipalAndSlugAndRate_getOldBookRateAndFourRate(String slug, Integer rate) throws BookstoreAPiWrongParameterException {
        Mockito.when(userRepositoryMock.findByEmail(any())).thenReturn(TEST_USER_ENTITY);
        Mockito.when(bookRateRepository.findBook2RateEntityByBookEntityAndUserEntity(any(), any())).thenReturn(TEST_BOOK_RATE_ENTITY_LIST);
        assertEquals(4, bookService.setBookRate(slug, rate, principal));
    }

    @Test
    void checkCredentials_withAnonymousUserPrincipal_getFalse() {
        assertEquals(false, bookService.checkCredentials("anonymousUser"));
    }

    @Test
    void checkCredentials_withNullPrincipal_getFalse() {
        assertEquals(false, bookService.checkCredentials(null));
    }

    @Test
    void checkCredentials_withOAuthPrincipal_getFalse() {
        DefaultOidcUser mockDefaultOidcUser = mock(DefaultOidcUser.class);
        assertEquals(false, bookService.checkCredentials(mockDefaultOidcUser));
    }

    @Test
    void checkCredentials_withAdminPrincipal_getTrue() {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(TEST_USER_ENTITY_ADMIN);
        assertEquals(true, bookService.checkCredentials(principal));
    }

    @ParameterizedTest
    @CsvSource({"null, /aaaa"})
    void removePostponedItemTempUser_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                         @ConvertWith(NullableConverter.class) String cookieString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removePostponedItemTempUser(slug, cookieString));
    }

    @ParameterizedTest
    @CsvSource({", /aaaa"})
    void removePostponedItemTempUser_withEmptySlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                          @ConvertWith(NullableConverter.class) String cookieString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removePostponedItemTempUser(slug, cookieString));
    }

    @ParameterizedTest
    @CsvSource({"aaaa, null"})
    void removePostponedItemTempUser_withNullCookieString_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                                 @ConvertWith(NullableConverter.class) String cookieString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removePostponedItemTempUser(slug, cookieString));
    }

    @ParameterizedTest
    @CsvSource({"aaaa,"})
    void removePostponedItemTempUser_withEmptyCookieString_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                                  @ConvertWith(NullableConverter.class) String cookieString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removePostponedItemTempUser(slug, cookieString));
    }

    @ParameterizedTest
    @CsvSource({"aaaa,/aaaa"})
    void removePostponedItemTempUser_withValidParameters_getEmptyCookie(String slug, String cookieString) throws BookstoreAPiWrongParameterException {
        assertFalse(bookService.removePostponedItemTempUser(slug, cookieString).toString().contains("aaaa"));
    }

    @ParameterizedTest
    @CsvSource({"null, /aaaa"})
    void removeCartItemTempUser_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                    @ConvertWith(NullableConverter.class) String cookieString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removeCartItemTempUser(slug, cookieString));
    }

    @ParameterizedTest
    @CsvSource({", /aaaa"})
    void removeCartItemTempUser_withEmptySlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                     @ConvertWith(NullableConverter.class) String cookieString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removeCartItemTempUser(slug, cookieString));
    }

    @ParameterizedTest
    @CsvSource({"aaaa, null"})
    void removeCartItemTempUser_withNullCookieString_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                            @ConvertWith(NullableConverter.class) String cookieString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removeCartItemTempUser(slug, cookieString));
    }

    @ParameterizedTest
    @CsvSource({"aaaa,"})
    void removeCartItemTempUser_withEmptyCookieString_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                             @ConvertWith(NullableConverter.class) String cookieString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.removeCartItemTempUser(slug, cookieString));
    }

    @ParameterizedTest
    @CsvSource({"aaaa,/aaaa"})
    void removeCartItemTempUser_withValidParameters_getEmptyCookie(String slug, String cookieString) throws BookstoreAPiWrongParameterException {
        assertFalse(bookService.removeCartItemTempUser(slug, cookieString).toString().contains("aaaa"));
    }

    @ParameterizedTest
    @CsvSource({"null, /aaaa"})
    void addPostponedItemTempUser_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                      @ConvertWith(NullableConverter.class) String postponedCookieString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.addPostponedItemTempUser(slug, postponedCookieString));
    }

    @ParameterizedTest
    @CsvSource({"aaaa, "})
    void addPostponedItemTempUser_withValidSlugAndEmptyCookie_getCookieWithSlug(String slug, String postponedCookieString) throws BookstoreAPiWrongParameterException {
        assertTrue(bookService.addPostponedItemTempUser(slug, postponedCookieString).getValue().contains("aaaa"));
    }

    @ParameterizedTest
    @CsvSource({"bbbb, /aaaa"})
    void addPostponedItemTempUser_withValidSlugAndNotEmptyCookie_getCookieWithSlug(String slug, String postponedCookieString) throws BookstoreAPiWrongParameterException {
        assertTrue(bookService.addPostponedItemTempUser(slug, postponedCookieString).getValue().contains("bbbb"));
    }

    @ParameterizedTest
    @CsvSource({"null, /aaaa"})
    void addCartItemTempUser_withNullSlug_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String slug,
                                                                                      @ConvertWith(NullableConverter.class) String cartCookieString) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> bookService.addCartItemTempUser(slug, cartCookieString));
    }

    @ParameterizedTest
    @CsvSource({"aaaa, "})
    void addCartItemTempUser_withValidSlugAndEmptyCookie_getCookieWithSlug(String slug, String cartCookieString) throws BookstoreAPiWrongParameterException {
        assertTrue(bookService.addCartItemTempUser(slug, cartCookieString).getValue().contains("aaaa"));
    }

    @ParameterizedTest
    @CsvSource({"bbbb, /aaaa"})
    void addCartItemTempUser_withValidSlugAndNotEmptyCookie_getCookieWithSlug(String slug, String cartCookieString) throws BookstoreAPiWrongParameterException {
        assertTrue(bookService.addCartItemTempUser(slug, cartCookieString).getValue().contains("bbbb"));
    }

}