package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.service.ResourceStorage;
import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;
    private final ResourceStorage storage;

    @Autowired
    public BookController(BookRepository bookRepository, ResourceStorage storage) {
        this.bookRepository = bookRepository;
        this.storage = storage;
    }

    @GetMapping("/{slug}")
    public String getBookPage(@PathVariable("slug") String slug,
                              Model model) {
        BookEntity bookEntity = bookRepository.getBookBySlug(slug);
        model.addAttribute("slugBook", bookEntity);
        return "/books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file,
                                   @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        BookEntity bookToUpdate = bookRepository.getBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);

        return ("redirect:/books/" + slug);
    }
}
