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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
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
    private final BookReviewRLDtoRepository bookReviewRLDtoRepository;
    private final BookReviewRLDtoMapper bookReviewRLDtoMapper;
    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final CartRepository cartRepository;

    private Integer DEFAULT_OFFSET = 0;
    private Integer DEFAULT_LIMIT = 10;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, TagRepository tagRepository, Book2TagRepository book2TagRepository, BookRateRepository bookRateRepository, BookReviewRepository bookReviewRepository, UserRepository userRepository, BookReviewRLDtoRepository bookReviewRLDtoRepository, BookReviewRLDtoMapper bookReviewRLDtoMapper, BookReviewLikeRepository bookReviewLikeRepository, CartRepository cartRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.book2TagRepository = book2TagRepository;
        this.bookRateRepository = bookRateRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.userRepository = userRepository;
        this.bookReviewRLDtoRepository = bookReviewRLDtoRepository;
        this.bookReviewRLDtoMapper = bookReviewRLDtoMapper;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.cartRepository = cartRepository;
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
            if (bookEntities.size() > 0) {
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
        if (offset != null || limit != null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);
        }
        return bookRepository.findBooksByGenreName(genreName, nextPage);
    }

    public Page<BookEntity> getPageOfBooksByAuthorName(String authorName, Integer offset, Integer limit) {
        Pageable nextPage;
        if (offset != null || limit != null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);
        }
        return bookRepository.findBooksByAuthor(authorName, nextPage);
    }

    public Page<BookEntity> getPageOfBooksByFolderId(Long folderId, Integer offset, Integer limit) {
        Pageable nextPage;
        if (offset != null || limit != null) {
            nextPage = PageRequest.of(offset, limit);
        } else {
            nextPage = PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT);
        }
        return bookRepository.findBooksByFolder(folderId, nextPage);
    }

    public Integer getAuthorBooksCount(String authorName) {
        return bookRepository.findBooksCountByAuthor(authorName);
    }

    public void addCartContentsItem(@PathVariable("slug") String slug,
                                    @CookieValue(name = "cartContents", required = false) String cartContents,
                                    HttpServletResponse response,
                                    Model model) {
        if (cartContents == null || cartContents.equals("")) {
            Cookie cookie = new Cookie("cartContents", "/" + slug);
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("cartContentsSize", 1);
        } else if (!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("cartContentsSize", cookie.getValue().split("/").length);
        }
    }

    public void removeCartContentsItemFromCart(@PathVariable("slug") String slug,
                                               @CookieValue(name = "cartContents", required = false) String cartContents,
                                               HttpServletResponse response,
                                               Model model) {
        if (cartContents != null || !cartContents.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("cartContentsSize", cookie.getValue().split("/").length);
        } else {
            model.addAttribute("isCartEmpty", true);
            model.addAttribute("cartContentsSize", 0);
        }
    }

    public void removePostponedItem(String slug) {

        Gson gson = new Gson();

        UserEntity userEntity = userRepository.findByName("Carita Gunn");
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);

        if (cartEntity != null) {
            CartItemEntity cartItemEntity = getCartItemEntity(cartEntity.getValue());
            if (cartItemEntity.getPostponedString().contains(slug)) {
                cartItemEntity.setPostponedString(cartItemEntity.getPostponedString().replace("/" + slug, ""));
                cartEntity.setValue(gson.toJson(cartItemEntity));
                cartRepository.save(cartEntity);
            }
        }
    }

    public void removeCartItem(String slug) {

        Gson gson = new Gson();

        UserEntity userEntity = userRepository.findByName("Carita Gunn");
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);

        if (cartEntity != null) {
            CartItemEntity cartItemEntity = getCartItemEntity(cartEntity.getValue());
            if (cartItemEntity.getCartString().contains(slug)) {
                cartItemEntity.setCartString(cartItemEntity.getCartString().replace("/" + slug, ""));
                cartEntity.setValue(gson.toJson(cartItemEntity));
                cartRepository.save(cartEntity);
            }
        }
    }

    public void addPostponedItem(String slug) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        UserEntity userEntity = userRepository.findByName("Carita Gunn");
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);

        if (cartEntity != null) {
            CartItemEntity cartItemEntity = getCartItemEntity(cartEntity.getValue());
            if (!cartItemEntity.getPostponedString().contains(slug)) {

                cartItemEntity.setPostponedString(cartItemEntity.getPostponedString() + "/" + slug);
                if (cartItemEntity.getCartString().contains(slug)) {
                    cartItemEntity.setCartString(cartItemEntity.getCartString().replace("/" + slug, ""));
                }
                cartEntity.setValue(gson.toJson(cartItemEntity));
                cartEntity.setEditDate(OffsetDateTime.now());
                cartRepository.save(cartEntity);
            }
        } else {
            CartEntity entity = createCartEntity(userEntity);
            CartItemEntity cartItemEntity = new CartItemEntity();
            cartItemEntity.setPostponedString("/" + slug);
            entity.setValue(gson.toJson(cartItemEntity));
            entity.setEditDate(OffsetDateTime.now());
            cartRepository.save(entity);
        }
    }

    public void addCartItem(String slug) {
        UserEntity userEntity = userRepository.findByName("Carita Gunn");
        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);
        Gson gson = new Gson();
        if (cartEntity != null) {
            CartItemEntity cartItemEntity = getCartItemEntity(cartEntity.getValue());
            if (!cartItemEntity.getCartString().contains(slug)) {
                cartItemEntity.setCartString(cartItemEntity.getCartString() + "/" + slug);
                if (cartItemEntity.getPostponedString().contains(slug)) {
                    cartItemEntity.setPostponedString(cartItemEntity.getPostponedString().replace("/" + slug, ""));
                }

                cartEntity.setValue(gson.toJson(cartItemEntity));
                cartEntity.setEditDate(OffsetDateTime.now());
                cartRepository.save(cartEntity);
            }
        } else {
            CartEntity entity = createCartEntity(userEntity);
            CartItemEntity cartItemEntity = new CartItemEntity();
            cartItemEntity.setCartString("/" + slug);
            entity.setValue(gson.toJson(cartItemEntity));
            entity.setEditDate(OffsetDateTime.now());
            cartRepository.save(entity);
        }
    }

    private CartItemEntity getCartItemEntity(String valueString) {
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

    public void addPostponedContentsItem(@PathVariable("slug") String slug,
                                         @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                         HttpServletResponse response,
                                         Model model) {
        if (postponedContents == null || postponedContents.equals("")) {
            Cookie cookie = new Cookie("postponedContents", "/" + slug);
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);
            model.addAttribute("postponedSize", 1);
        } else if (!postponedContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(postponedContents).add(slug);
            Cookie cookie = new Cookie("postponedContents", stringJoiner.toString());
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);
            model.addAttribute("postponedSize", cookie.getValue().split("/").length);
        }
    }

    public void removePostponedContentsItemFromPostponed(@PathVariable("slug") String slug,
                                                         @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                                         HttpServletResponse response,
                                                         Model model) {
        if (postponedContents != null || !postponedContents.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(postponedContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("postponedContents", String.join("/", cookieBooks));
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);
            model.addAttribute("postponedSize", cookie.getValue().split("/").length);
        } else {
            model.addAttribute("isPostponedEmpty", true);
            model.addAttribute("postponedSize", 0);
        }
    }

    public Integer getCartCount() {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        UserEntity userEntity = userRepository.findByName("Carita Gunn");
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
                    return gson.fromJson(object, CartItemEntity.class).getCartString().split("/").length - 1;
                }
            } else {
                if (!cartEntity.getValue().equals("") && cartEntity.getValue() != null) {
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

    public Integer getPostponedCount() {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        UserEntity userEntity = userRepository.findByName("Carita Gunn");
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
                    return gson.fromJson(object, CartItemEntity.class).getPostponedString().split("/").length - 1;
                }
            } else {
                if (!cartEntity.getValue().equals("") && cartEntity.getValue() != null) {
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

    public List<BookEntity> getBookListInCart() {
        UserEntity userEntity = userRepository.findByName("Carita Gunn");
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

    public List<BookEntity> getBookListInPostponed() {
        UserEntity userEntity = userRepository.findByName("Carita Gunn");
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

    public Integer getBookRate(String slug) {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        UserEntity userEntity = userRepository.findByName("Carita Gunn");
        List<Book2RateEntity> book2RateEntity = bookRateRepository.findBook2RateEntityByBookEntityAndUserEntity(bookEntity.getId(), userEntity.getId());
        if (book2RateEntity != null && book2RateEntity.size() > 0) {
            return book2RateEntity.get(0).getRate();
        } else {
            return 0;
        }

    }

    public Integer setBookRate(String slug, Integer rate) {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        UserEntity userEntity = userRepository.findByName("Carita Gunn");
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

    public Boolean createBookReview(String slug, String comment) throws BookstoreAPiWrongParameterException {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        if (bookEntity != null && comment != null && !comment.equals("")) {
            UserEntity userEntity = userRepository.findByName("Carita Gunn");
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
        UserEntity userEntity = userRepository.findByName("Carita Gunn");
        if (bookEntity != null) {
            List<BookReviewEntity> bookReviewEntities = bookReviewRepository.findByUserEntityAndBookEntity(bookEntity);
            List<BookReviewRLDto> bookReviewRLDtos = new ArrayList<>();
            if (bookReviewEntities != null || bookReviewEntities.size() == 0) {
                bookReviewRLDtos = bookReviewRLDtoMapper.map(bookReviewEntities);
            }
            return bookReviewRLDtos;
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    public void handleReviewLikesDislikes(String slug, Long reviewid, Long value) throws BookstoreAPiWrongParameterException {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        UserEntity userEntity = userRepository.findByName("Carita Gunn");
        if (bookEntity != null && reviewid != null && userEntity != null) {
            BookReviewLikeEntity bookReviewLikeEntity = bookReviewLikeRepository.getReviewLikeEntityByReviewIdAndUserId(reviewid, userEntity.getId());
            if (bookReviewLikeEntity != null) {
                bookReviewLikeEntity.setValue(value > 0 ? (short) 1 : 0);
                bookReviewLikeRepository.save(bookReviewLikeEntity);
            } else {
                BookReviewLikeEntity entity = new BookReviewLikeEntity();
                entity.setValue(value > 0 ? (short) 1 : 0);
                entity.setTime(LocalDateTime.now());
                entity.setReviewId(reviewid);
                entity.setUser(userEntity);
                bookReviewLikeRepository.save(entity);
            }
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    public Integer getBookRateTotal(String slug) {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        List<Book2RateEntity> book2RateEntities = bookRateRepository.findBook2RateEntitiesByBookEntity(bookEntity);
        if (book2RateEntities != null && book2RateEntities.size() > 0) {
            int sum = book2RateEntities.stream().mapToInt(o -> o.getRate()).sum();
            return (int) (Math.round(sum / book2RateEntities.size()));
        } else {
            return 0;
        }

    }

    public Integer getBookRateTotalCount(String slug) {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        List<Book2RateEntity> book2RateEntities = bookRateRepository.findBook2RateEntitiesByBookEntity(bookEntity);
        if (book2RateEntities != null && book2RateEntities.size() > 0) {
            return book2RateEntities.size();
        } else {
            return 0;
        }
    }

    public Integer getBookRateSubTotal(String slug, int rate) {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        List<Book2RateEntity> book2RateEntities = bookRateRepository.findBook2RateEntitiesByBookEntityAndRate(bookEntity, rate);
        if (book2RateEntities != null && book2RateEntities.size() > 0) {
            int sum = book2RateEntities.stream().mapToInt(o -> o.getRate()).sum();
            return (int) (Math.round(sum / book2RateEntities.size()));
        } else {
            return 0;
        }

    }

    public Integer getBookRateSubTotalCount(String slug, int rate) {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        List<Book2RateEntity> book2RateEntities = bookRateRepository.findBook2RateEntitiesByBookEntityAndRate(bookEntity, rate);
        if (book2RateEntities != null && book2RateEntities.size() > 0) {
            return book2RateEntities.size();
        } else {
            return 0;
        }

    }

    public Boolean checkCredentials(Object principal) {

        UserEntity userEntity = userRepository.findByEmail(principal.getClass().getName());

        if (userEntity != null) {
            return userEntity.getEmail().equals("admin@example.com");
        } else {
            return false;
        }
    }
}
