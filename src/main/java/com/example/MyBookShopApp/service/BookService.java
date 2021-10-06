package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.BookReviewRLDto;
import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.links.Book2RateEntity;
import com.example.MyBookShopApp.entity.book.links.Book2TagEntity;
import com.example.MyBookShopApp.entity.book.review.BookReviewEntity;
import com.example.MyBookShopApp.entity.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.entity.cart.CartEntity;
import com.example.MyBookShopApp.entity.cart.CartItemEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.mapper.BookReviewRLDtoMapper;
import com.example.MyBookShopApp.repository.*;
import com.example.MyBookShopApp.security.UserEntityDetails;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.validator.routines.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final Book2TagRepository book2TagRepository;
    private final BookRateRepository bookRateRepository;
    private final BookReviewRepository bookReviewRepository;
    private final UserRepository userRepository;
    private final BookReviewRLDtoMapper bookReviewRLDtoMapper;
    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final CartRepository cartRepository;

    private Integer DEFAULT_OFFSET = 0;
    private Integer DEFAULT_LIMIT = 10;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, TagRepository tagRepository, Book2TagRepository book2TagRepository, BookRateRepository bookRateRepository, BookReviewRepository bookReviewRepository, UserRepository userRepository, BookReviewRLDtoMapper bookReviewRLDtoMapper, BookReviewLikeRepository bookReviewLikeRepository, CartRepository cartRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.book2TagRepository = book2TagRepository;
        this.bookRateRepository = bookRateRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.userRepository = userRepository;
        this.bookReviewRLDtoMapper = bookReviewRLDtoMapper;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.cartRepository = cartRepository;
    }

    public List<BookEntity> getBooksData() {
        return bookRepository.findAll();
    }

    public List<BookEntity> getBooksByAuthor(String authorName) throws BookstoreAPiWrongParameterException {
        if (authorName.equals("")) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        AuthorEntity entity = authorRepository.findAuthorEntityByName(authorName);
        if (entity == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        return bookRepository.findBookEntitiesByAuthorSetContaining(entity);
    }


    public List<BookEntity> getBooksByTitle(String title) throws BookstoreAPiWrongParameterException {
        if (title.equals("") || title.length() <= 1) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<BookEntity> bookEntities = bookRepository.findBookEntitiesByTitleContaining(title);
            if (bookEntities.size() > 0) {
                return bookEntities;
            } else {
                throw new BookstoreAPiWrongParameterException("No data found with specified parameters...");
            }
        }
    }

    public List<BookEntity> getBooksWithPriceBetween(Integer min, Integer max) throws BookstoreAPiWrongParameterException {
        if (min == null || max == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        return bookRepository.findBookEntitiesByPriceBetween(min, max);
    }

    public List<BookEntity> getBooksWithMaxDiscount() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<BookEntity> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public Page<BookEntity> getPageOfRecommendedBooks(Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        if (offset == null || limit == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooks(Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        if (offset == null || limit == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAllDesc(nextPage);
    }

    public Page<BookEntity> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        if (searchWord == null || offset == null || limit == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookEntitiesByTitleContaining(searchWord, nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooksInterval(String dateFrom, String dateTo, Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        if (dateFrom == null || dateTo == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        Pageable nextPage0 = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);

        OffsetDateTime dateTimeFrom = getDateOffset(dateFrom);
        OffsetDateTime dateTimeTo = getDateOffset(dateTo);

        return bookRepository.findBookEntitiesByPubDateAfterAndPubDateBefore(dateTimeFrom, dateTimeTo, nextPage0);
    }

    public OffsetDateTime getDateOffset(String inputString) throws BookstoreAPiWrongParameterException {
        if (inputString == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }

        DateValidator validator = new DateValidatorUsingDateFormat("dd.MM.yyyy");

        if (!validator.isValid(inputString)) {
            throw new BookstoreAPiWrongParameterException("Invalid date format");
        }

        SimpleDateFormat fromUser = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        String reformattedStr = null;
        try {
            reformattedStr = myFormat.format(fromUser.parse(inputString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return OffsetDateTime.parse(reformattedStr + "T01:00+03:00");
    }

    public Page<BookEntity> getPageOfPopularBooksOrdered(Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        if (offset == null || limit == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPopularRate(nextPage);
    }

    public Page<BookEntity> getPageOfTaggedBooks(String tagName, Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        if (tagName == null || offset == null || limit == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTagName(tagName, nextPage);
    }

    public String getBookTag(Long bookId) throws BookstoreAPiWrongParameterException {
        if (bookId == null || bookId <= 0) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        Book2TagEntity book2TagEntity = book2TagRepository.findByBookId(bookId);
        return tagRepository.findById(book2TagEntity.getTagId()).get().getName();
    }

    public Page<BookEntity> getPageOfBooksByGenreName(String genreName, Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        if (genreName == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        Pageable nextPage;
        if (offset != null || limit != null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);
        }
        return bookRepository.findBooksByGenreName(genreName, nextPage);
    }

    public Page<BookEntity> getPageOfBooksByAuthorName(String authorName, Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        if (authorName == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        Pageable nextPage;
        if (offset != null || limit != null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);
        }
        return bookRepository.findBooksByAuthor(authorName, nextPage);
    }

    public Page<BookEntity> getPageOfBooksByFolderId(Long folderId, Integer offset, Integer limit) throws BookstoreAPiWrongParameterException {
        if (folderId == null || folderId < 0) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        Pageable nextPage;
        if (offset != null || limit != null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);
        }
        return bookRepository.findBooksByFolder(folderId, nextPage);
    }

    public Integer getAuthorBooksCount(String authorName) throws BookstoreAPiWrongParameterException {
        if (authorName == null) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        return bookRepository.findBooksCountByAuthor(authorName);
    }

    
    public boolean removePostponedItem(String slug, Object principal) throws BookstoreAPiWrongParameterException {
        validateSlug(slug);
        Gson gson = new Gson();

        UserEntity userEntity = validateUserPrincipal(principal);
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);

        if (cartEntity != null) {
            CartItemEntity cartItemEntity = getCartItemEntity(cartEntity.getValue());
            if (cartItemEntity.getPostponedString().contains(slug)) {
                cartItemEntity.setPostponedString(cartItemEntity.getPostponedString().replace("/" + slug, ""));
                cartEntity.setValue(gson.toJson(cartItemEntity));
                cartRepository.save(cartEntity);
                return true;
            }
        }
        return false;
    }

    public Cookie removePostponedItemTempUser(String slug, String postponedCookieString) throws BookstoreAPiWrongParameterException {
        validateSlug(slug);
        validateCookieString(postponedCookieString);
        ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(postponedCookieString.split("/")));
        cookieBooks.remove(slug);
        Cookie cookie = new Cookie("postponedContents", String.join("/", cookieBooks));
        cookie.setPath("/");
        return cookie;
    }

    
    public boolean removeCartItem(String slug, Object principal) throws BookstoreAPiWrongParameterException {
        validateSlug(slug);
        Gson gson = new Gson();

        UserEntity userEntity = validateUserPrincipal(principal);
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);

        if (cartEntity != null) {
            CartItemEntity cartItemEntity = getCartItemEntity(cartEntity.getValue());
            if (cartItemEntity.getCartString().contains(slug)) {
                cartItemEntity.setCartString(cartItemEntity.getCartString().replace("/" + slug, ""));
                cartEntity.setValue(gson.toJson(cartItemEntity));
                cartRepository.save(cartEntity);
                return true;
            }
        }
        return false;
    }

    public Cookie removeCartItemTempUser(String slug, String cartCookieString) throws BookstoreAPiWrongParameterException {
        validateSlug(slug);
        validateCookieString(cartCookieString);
        ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartCookieString.split("/")));
        cookieBooks.remove(slug);
        Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
        cookie.setPath("/");
        return cookie;
    }

    
    public Boolean addPostponedItem(String slug, Object principal) throws BookstoreAPiWrongParameterException {
        validateSlug(slug);

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        UserEntity userEntity = validateUserPrincipal(principal);

        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);

        if (cartEntity != null) {
            if (!cartEntity.getValue().equals("")) {
                CartItemEntity cartItemEntity = getCartItemEntity(cartEntity.getValue());
                if (cartItemEntity.getPostponedString() != null) {
                    if (!cartItemEntity.getPostponedString().contains(slug)) {

                        cartItemEntity.setPostponedString(cartItemEntity.getPostponedString() + "/" + slug);
                        if (cartItemEntity.getCartString() != null && cartItemEntity.getCartString().contains(slug)) {
                            cartItemEntity.setCartString(cartItemEntity.getCartString().replace("/" + slug, ""));
                        }
                        cartEntity.setValue(gson.toJson(cartItemEntity));
                        cartEntity.setEditDate(OffsetDateTime.now());
                        cartRepository.save(cartEntity);
                        return true;
                    }
                } else {
                    cartItemEntity.setPostponedString("/" + slug);
                    if (cartItemEntity.getCartString() != null && cartItemEntity.getCartString().contains(slug)) {
                        cartItemEntity.setCartString(cartItemEntity.getCartString().replace("/" + slug, ""));
                    }
                    cartEntity.setValue(gson.toJson(cartItemEntity));
                    cartEntity.setEditDate(OffsetDateTime.now());
                    cartRepository.save(cartEntity);
                    return true;
                }
            } else {
                CartItemEntity cartItemEntity = new CartItemEntity();
                cartItemEntity.setPostponedString("/" + slug);
                cartEntity.setValue(gson.toJson(cartItemEntity));
                cartEntity.setEditDate(OffsetDateTime.now());
                cartRepository.save(cartEntity);
                return true;
            }
        } else {
            CartEntity entity = createCartEntity(userEntity);
            CartItemEntity cartItemEntity = new CartItemEntity();
            cartItemEntity.setPostponedString("/" + slug);
            entity.setValue(gson.toJson(cartItemEntity));
            entity.setEditDate(OffsetDateTime.now());
            cartRepository.save(entity);
            return true;
        }
        return false;
    }

    public Cookie addPostponedItemTempUser(String slug, String postponedCookieString) throws BookstoreAPiWrongParameterException {
        validateSlug(slug);

        if (postponedCookieString == null || postponedCookieString.equals("")) {
            Cookie cookie = new Cookie("postponedContents", slug);
            cookie.setMaxAge(2 * 60 * 60);
            cookie.setPath("/");
            return cookie;
        } else if (!postponedCookieString.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(postponedCookieString).add(slug);
            Cookie cookie = new Cookie("postponedContents", stringJoiner.toString());
            cookie.setMaxAge(2 * 60 * 60);
            cookie.setPath("/");
            return cookie;
        }
        return null;
    }

    
    public boolean addCartItem(String slug, Object principal) throws BookstoreAPiWrongParameterException {
        validateSlug(slug);

        UserEntity userEntity = validateUserPrincipal(principal);
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);

        Gson gson = new Gson();
        if (cartEntity != null) {
            if (!cartEntity.getValue().equals("")) {
                CartItemEntity cartItemEntity = getCartItemEntity(cartEntity.getValue());
                if (cartItemEntity.getCartString() != null) {
                    if (!cartItemEntity.getCartString().contains(slug)) {
                        cartItemEntity.setCartString(cartItemEntity.getCartString() + "/" + slug);
                        if (cartItemEntity.getPostponedString().contains(slug)) {
                            cartItemEntity.setPostponedString(cartItemEntity.getPostponedString().replace("/" + slug, ""));
                        }
                        cartEntity.setValue(gson.toJson(cartItemEntity));
                        cartEntity.setEditDate(OffsetDateTime.now());
                        cartRepository.save(cartEntity);
                        return true;
                    }
                } else {
                    cartItemEntity.setCartString("/" + slug);
                    if (cartItemEntity.getPostponedString().contains(slug)) {
                        cartItemEntity.setPostponedString(cartItemEntity.getPostponedString().replace("/" + slug, ""));
                    }
                    cartEntity.setValue(gson.toJson(cartItemEntity));
                    cartEntity.setEditDate(OffsetDateTime.now());
                    cartRepository.save(cartEntity);
                    return true;
                }
            } else {
                CartItemEntity cartItemEntity = new CartItemEntity();
                cartItemEntity.setCartString("/" + slug);
                cartEntity.setValue(gson.toJson(cartItemEntity));
                cartEntity.setEditDate(OffsetDateTime.now());
                cartRepository.save(cartEntity);
                return true;
            }
        } else {
            CartEntity entity = createCartEntity(userEntity);
            CartItemEntity cartItemEntity = new CartItemEntity();
            cartItemEntity.setCartString("/" + slug);
            entity.setValue(gson.toJson(cartItemEntity));
            entity.setEditDate(OffsetDateTime.now());
            cartRepository.save(entity);
            return true;
        }
        return false;
    }

    public Cookie addCartItemTempUser(String slug, String cartContents) throws BookstoreAPiWrongParameterException {
        validateSlug(slug);

        if (cartContents == null || cartContents.equals("")) {
            Cookie cookie = new Cookie("cartContents", slug);
            cookie.setMaxAge(2 * 60 * 60);
            cookie.setPath("/");
            return cookie;
        } else if (!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            cookie.setMaxAge(2 * 60 * 60);
            cookie.setPath("/");
            return cookie;
        }
        return null;
    }

    public CartItemEntity getCartItemEntity(String valueString) {
        if (valueString.length() > 0 && valueString != null) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(valueString);
            return gson.fromJson(object, CartItemEntity.class);
        } else {
            return new CartItemEntity();
        }
    }

    private CartEntity createCartEntity(UserEntity userEntity) {
        Gson gson = new Gson();
        CartEntity entity = new CartEntity();
        entity.setUserEntity(userEntity);

        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setPostponedString("");
        cartItemEntity.setCartString("");

        entity.setValue(gson.toJson(cartItemEntity));

        cartRepository.save(entity);
        return entity;
    }

    
    public Integer getCartCount(Object principal) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        UserEntity userEntity = validateUserPrincipal(principal);
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);

        if (cartEntity != null) {
            if (cartEntity.getEditDate() != null) {
                OffsetDateTime cartDateExpire = cartEntity.getEditDate().plusDays(1L);
                OffsetDateTime dateNow = OffsetDateTime.now();
                if (cartDateExpire.isBefore(dateNow) == true) {
                    cartEntity.setValue("");
                    cartEntity.setEditDate(null);
                    cartRepository.save(cartEntity);
                    return 0;
                } else {
                    JsonObject object = (JsonObject) parser.parse(cartEntity.getValue());
                    String cartString = gson.fromJson(object, CartItemEntity.class).getCartString();
                    if (cartString != null && !cartString.equals("")) {
                        return gson.fromJson(object, CartItemEntity.class).getCartString().split("/").length - 1;
                    } else {
                        return 0;
                    }
                }
            } else {
                if (cartEntity.getValue() != null && !cartEntity.getValue().equals("")) {
                    JsonObject object = (JsonObject) parser.parse(cartEntity.getValue());
                    return gson.fromJson(object, CartItemEntity.class).getCartString().split("/").length - 1;
                } else {
                    return 0;
                }
            }
        } else {
            createCartEntity(userEntity);
            return 0;
        }
    }

    public Integer getCartCountTempUser(String cartContents) {
        if (cartContents == null || cartContents.equals("")) {
            return 0;
        } else {
            return cartContents.split("/").length;
        }
    }

    
    public Integer getPostponedCount(Object principal) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        UserEntity userEntity = validateUserPrincipal(principal);
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);

        if (cartEntity != null) {
            if (cartEntity.getEditDate() != null) {
                OffsetDateTime cartDateExpire = cartEntity.getEditDate().plusDays(1L);
                OffsetDateTime dateNow = OffsetDateTime.now();
                if (cartDateExpire.isBefore(dateNow) == true) {
                    cartEntity.setValue("");
                    cartEntity.setEditDate(null);
                    cartRepository.save(cartEntity);
                    return 0;
                } else {
                    JsonObject object = (JsonObject) parser.parse(cartEntity.getValue());
                    String postponedString = gson.fromJson(object, CartItemEntity.class).getPostponedString();
                    if (postponedString != null && !postponedString.equals("")) {
                        return gson.fromJson(object, CartItemEntity.class).getPostponedString().split("/").length - 1;
                    } else {
                        return 0;
                    }
                }
            } else {
                if (cartEntity.getValue() != null && !cartEntity.getValue().equals("")) {
                    JsonObject object = (JsonObject) parser.parse(cartEntity.getValue());
                    return gson.fromJson(object, CartItemEntity.class).getPostponedString().split("/").length - 1;
                } else {
                    return 0;
                }
            }
        } else {
            createCartEntity(userEntity);
            return 0;
        }
    }

    public Integer getPostponedCountTempUser(String postponedContents) {
        if (postponedContents == null || postponedContents.equals("")) {
            return 0;
        } else {
            return postponedContents.split("/").length;
        }
    }

    
    public List<BookEntity> getBookListInCart(Object principal) {
        UserEntity userEntity = validateUserPrincipal(principal);
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);
        if (cartEntity != null) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(cartEntity.getValue());
            String[] cookieSlugs = gson.fromJson(object, CartItemEntity.class).getCartString().split("/");
            return bookRepository.findBookEntityBySlugIn(cookieSlugs);
        }
        return new ArrayList<>();
    }


    public List<BookEntity> getBookListInPostponed(Object principal) {
        UserEntity userEntity = validateUserPrincipal(principal);
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);
        if (cartEntity != null) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(cartEntity.getValue());
            String[] cookieSlugs = gson.fromJson(object, CartItemEntity.class).getPostponedString().split("/");
            return bookRepository.findBookEntityBySlugIn(cookieSlugs);
        }
        return new ArrayList<>();
    }

    public List<BookEntity> getBookListInCartUserTemp(String cartContents) {
        if (cartContents == null || cartContents.equals("")) {
            return new ArrayList<>();
        }
        String[] cookieSlugs = cartContents.split("/");
        return bookRepository.findBookEntityBySlugIn(cookieSlugs);
    }

    public List<BookEntity> getBookListInPostponedUserTemp(String postponedContents) {
        if (postponedContents == null || postponedContents.equals("")) {
            return new ArrayList<>();
        }
        String[] cookieSlugs = postponedContents.split("/");
        return bookRepository.findBookEntityBySlugIn(cookieSlugs);
    }

    
    public Integer getBookRate(String slug, Object principal) throws BookstoreAPiWrongParameterException {
        validateSlug(slug);
        if (validateJWTUserPrincipal(principal)) {
            BookEntity bookEntity = bookRepository.getBookBySlug(slug);
            UserEntity userEntity = userRepository.findByEmail(((UserEntityDetails) principal).getUsername());
            List<Book2RateEntity> book2RateEntity = bookRateRepository.findBook2RateEntityByBookEntityAndUserEntity(bookEntity.getId(), userEntity.getId());
            if (book2RateEntity != null && book2RateEntity.size() > 0) {
                return book2RateEntity.get(0).getRate();
            } else {
                return 0;
            }
        }
        return 0;
    }

    private boolean validateJWTUserPrincipal(Object principal) {
        if (principal == null || principal.equals("anonymousUser") || principal.getClass().getName().contains("oidc")) {
            return false;
        }
        return true;
    }

    
    public Integer setBookRate(String slug, Integer rate, Object principal) throws BookstoreAPiWrongParameterException {
        validateSlug(slug);
        validateRate(rate);
        if (validateJWTUserPrincipal(principal)) {
            BookEntity bookEntity = bookRepository.getBookBySlug(slug);
            UserEntity userEntity = userRepository.findByEmail(((UserEntityDetails) principal).getUsername());
            List<Book2RateEntity> book2RateEntity = bookRateRepository.findBook2RateEntityByBookEntityAndUserEntity(bookEntity.getId(), userEntity.getId());
            if (book2RateEntity == null || book2RateEntity.size() == 0) {
                Book2RateEntity entity = new Book2RateEntity();
                entity.setRate(rate);
                entity.setBookEntity(bookEntity);
                entity.setUserEntity(userEntity);
                bookRateRepository.save(entity);
            } else {
                book2RateEntity.get(0).setRate(rate);
                bookRateRepository.save(book2RateEntity.get(0));
            }
            return rate;
        }
        return 0;
    }

    private void validateRate(Integer rate) throws BookstoreAPiWrongParameterException {
        if (rate == null || rate < 0 || rate > 5) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }

    }

    public Boolean createBookReview(String slug, String comment, UserEntity userEntity) throws BookstoreAPiWrongParameterException {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        if (bookEntity != null && comment != null && !comment.equals("")) {
            BookReviewEntity bookReviewEntity = new BookReviewEntity();

            bookReviewEntity.setTime(LocalDateTime.now());
            bookReviewEntity.setBookEntity(bookEntity);
            bookReviewEntity.setUserEntity(userEntity);
            bookReviewEntity.setText(comment);

            Logger.getLogger(this.getClass().getSimpleName()).info("Book Review Entity to save" + bookReviewEntity);
            bookReviewRepository.save(bookReviewEntity);

            return true;
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    public List<BookReviewRLDto> getBookReviewInfo(String slug) throws BookstoreAPiWrongParameterException {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        if (bookEntity != null) {
            List<BookReviewEntity> bookReviewEntities = bookReviewRepository.findByBookId(bookEntity.getId());
            List<BookReviewRLDto> bookReviewRLDtos = new ArrayList<>();
            if (bookReviewEntities != null || bookReviewEntities.size() == 0) {
                bookReviewRLDtos = bookReviewRLDtoMapper.map(bookReviewEntities);
            }
            return bookReviewRLDtos;
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    public void handleReviewLikesDislikes(String slug, Long reviewId, Long value, Long userId) throws BookstoreAPiWrongParameterException {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        UserEntity userEntity = userRepository.findByIdExactly(userId);
        if (bookEntity != null && reviewId != null && userEntity != null) {
            BookReviewLikeEntity bookReviewLikeEntity = bookReviewLikeRepository.getReviewLikeEntityByReviewIdAndUserId(reviewId, userEntity.getId());
            if (bookReviewLikeEntity != null) {
                bookReviewLikeEntity.setValue(value > 0 ? (short) 1 : 0);
                bookReviewLikeRepository.save(bookReviewLikeEntity);
            } else {
                BookReviewLikeEntity entity = new BookReviewLikeEntity();
                entity.setValue(value > 0 ? (short) 1 : 0);
                entity.setTime(LocalDateTime.now());
                entity.setReviewId(reviewId);
                entity.setUser(userEntity);
                bookReviewLikeRepository.save(entity);
            }
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    public Integer getBookRateTotal(String slug) throws BookstoreAPiWrongParameterException {
        if (slug == null || slug.equals("")) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        if (bookEntity != null) {
            List<Book2RateEntity> book2RateEntities = bookRateRepository.findBook2RateEntitiesByBookEntity(bookEntity);
            if (book2RateEntities != null && book2RateEntities.size() > 0) {
                int sum = book2RateEntities.stream().mapToInt(o -> o.getRate()).sum();
                return (int) (Math.round(sum / book2RateEntities.size()));
            } else {
                return 0;
            }
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    public Integer getBookRateTotalCount(String slug) throws BookstoreAPiWrongParameterException {
        if (slug == null || slug.equals("")) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }

        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        if (bookEntity != null) {
            List<Book2RateEntity> book2RateEntities = bookRateRepository.findBook2RateEntitiesByBookEntity(bookEntity);
            if (book2RateEntities != null && book2RateEntities.size() > 0) {
                return book2RateEntities.size();
            } else {
                return 0;
            }
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    public Integer getBookRateSubTotal(String slug, int rate) throws BookstoreAPiWrongParameterException {
        if (slug == null || slug.equals("") || rate < 0 || rate > 5) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }

        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        if (bookEntity != null) {
            List<Book2RateEntity> book2RateEntities = bookRateRepository.findBook2RateEntitiesByBookEntityAndRate(bookEntity, rate);
            if (book2RateEntities != null && book2RateEntities.size() > 0) {
                int sum = book2RateEntities.stream().mapToInt(o -> o.getRate()).sum();
                return (int) (Math.round(sum / book2RateEntities.size()));
            } else {
                return 0;
            }
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    public Integer getBookRateSubTotalCount(String slug, int rate) throws BookstoreAPiWrongParameterException {
        if (slug == null || slug.equals("") || rate < 0 || rate > 5) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        if (bookEntity != null) {
            List<Book2RateEntity> book2RateEntities = bookRateRepository.findBook2RateEntitiesByBookEntityAndRate(bookEntity, rate);
            if (book2RateEntities != null && book2RateEntities.size() > 0) {
                return book2RateEntities.size();
            } else {
                return 0;
            }
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    
    public Boolean checkCredentials(Object principal) {
        try {
            UserEntity userEntity = userRepository.findByEmail(principal.getClass().getName());
            if (userEntity != null) {
                return userEntity.getEmail().equals("admin@example.com");
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private UserEntity validateUserPrincipal(Object principal) {
        UserEntity userEntity = userRepository.findByEmail(((UserEntityDetails) principal).getUsername());
        if (userEntity == null) {
            throw new UsernameNotFoundException("Bad credentials");
        }
        return userEntity;
    }

    private void validateSlug(String slug) throws BookstoreAPiWrongParameterException {
        if (slug == null || slug.equals("")) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    private void validateCookieString(String cookieString) throws BookstoreAPiWrongParameterException {
        if (cookieString == null || cookieString.equals("")) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

}
