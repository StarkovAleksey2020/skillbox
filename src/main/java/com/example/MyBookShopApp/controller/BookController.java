package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.exception.ForbiddenException;
import com.example.MyBookShopApp.exception.InsufficientRightsToChangeCoverException;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.security.UserEntityDetails;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.ResourceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;
    private final ResourceStorage storage;
    private final BookService bookService;

    @Autowired
    public BookController(BookRepository bookRepository, ResourceStorage storage, BookService bookService) {
        this.bookRepository = bookRepository;
        this.storage = storage;
        this.bookService = bookService;
    }

    @ModelAttribute("postponedSize")
    public Integer getPostponedSize(@CookieValue(name = "postponedContents", required = false) String postponedContents) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                return bookService.getPostponedCount();
            }
        } catch (Exception e) {
            return bookService.getPostponedCountTempUser(postponedContents);
        }
        return 0;
    }

    @ModelAttribute("cartContentsSize")
    public Integer getCartContentsSize(@CookieValue(name = "cartContents", required = false) String cartContents) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (((UserEntityDetails) principal).getUsername() != null && !((UserEntityDetails) principal).getUsername().equals("")) {
                return bookService.getCartCount();
            }
        } catch (Exception e) {
            return bookService.getCartCountTempUser(cartContents);
        }
        return 0;
    }

    @GetMapping("/{slug}")
    public String getBookPage(@PathVariable("slug") String slug,
                              Model model) throws BookstoreAPiWrongParameterException {
        if ((slug != null || !slug.equals("")) && !slug.equals("favicon.ico")) {
            BookEntity bookEntity = bookRepository.getBookBySlug(slug);
            model.addAttribute("slugBook", bookEntity);
            model.addAttribute("bookRate", bookService.getBookRate(slug));
            model.addAttribute("bookRateTotal", bookService.getBookRateTotal(slug));
            model.addAttribute("bookRateTotalCount", bookService.getBookRateTotalCount(slug));
            model.addAttribute("bookRateSubTotal1", bookService.getBookRateSubTotal(slug, 1));
            model.addAttribute("bookRateSubTotalCount1", bookService.getBookRateSubTotalCount(slug, 1));
            model.addAttribute("bookRateSubTotal2", bookService.getBookRateSubTotal(slug, 2));
            model.addAttribute("bookRateSubTotalCount2", bookService.getBookRateSubTotalCount(slug, 2));
            model.addAttribute("bookRateSubTotal3", bookService.getBookRateSubTotal(slug, 3));
            model.addAttribute("bookRateSubTotalCount3", bookService.getBookRateSubTotalCount(slug, 3));
            model.addAttribute("bookRateSubTotal4", bookService.getBookRateSubTotal(slug, 4));
            model.addAttribute("bookRateSubTotalCount4", bookService.getBookRateSubTotalCount(slug, 4));
            model.addAttribute("bookRateSubTotal5", bookService.getBookRateSubTotal(slug, 5));
            model.addAttribute("bookRateSubTotalCount5", bookService.getBookRateSubTotalCount(slug, 5));
            model.addAttribute("bookReviewInfo", bookService.getBookReviewInfo(slug));
        }
        return "/books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file,
                                   @PathVariable("slug") String slug,
                                   Model model) throws IOException, ForbiddenException, InsufficientRightsToChangeCoverException {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean isSufficientLevelOfAccess = bookService.checkCredentials(principal);

        if (isSufficientLevelOfAccess) {
            String savePath = storage.saveNewBookImage(file, slug);
            BookEntity bookToUpdate = bookRepository.getBookBySlug(slug);
            bookToUpdate.setImage(savePath);
            bookRepository.save(bookToUpdate);
        } else {
            throw new InsufficientRightsToChangeCoverException(slug);
        }

        return ("redirect:/books/" + slug);
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> getBookFile(@PathVariable("hash") String hash) throws IOException {

        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data length: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @PostMapping(value = "/rateBookReview")
    public String handleLikesDislikes(@RequestParam("slug") String slug,
                                      @RequestParam("reviewid") Long reviewid,
                                      @RequestParam("value") Long value,
                                      Model model) throws BookstoreAPiWrongParameterException {
        if (slug != null || !slug.equals("")) {
            bookService.handleReviewLikesDislikes(slug, reviewid, value);
            model.addAttribute("bookReviewInfo", bookService.getBookReviewInfo(slug));
        }

        return ("redirect:/books/" + slug);
    }
}
