package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GenresPage {

    private final BookService bookService;

    public GenresPage(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/genres")
    public String genresPage() {
        return "genres/index";
    }

    @GetMapping("/genres/slug")
    public String slugPage(@RequestParam(value = "sectionName", required = false) String sectionName,
                           @RequestParam(value = "genresName", required = false) String genresName,
                           @RequestParam(value = "subSectionName", required = false) String subSectionName,
                           @RequestParam(value = "genreName", required = false) String genreName,
                           @RequestParam(value = "offset", required = false) Integer offset,
                           @RequestParam(value = "limit", required = false) Integer limit,
                           Model model) {
        model.addAttribute("sectionName", sectionName);
        model.addAttribute("genresName", genresName);
        model.addAttribute("subSectionName", subSectionName);
        model.addAttribute("booksByGenreList", bookService.getPageOfBooksByGenreName(genreName, offset, limit));
        model.addAttribute("folderId", -1);
        return "genres/slug";
    }

    @GetMapping("/genres/folder/slug")
    public String slugFolderPage(@RequestParam(value = "sectionName", required = false) String sectionName,
                           @RequestParam(value = "genresName", required = false) String genresName,
                           @RequestParam(value = "subSectionName", required = false) String subSectionName,
                           @RequestParam(value = "folderId", required = false) Long folderId,
                           @RequestParam(value = "offset", required = false) Integer offset,
                           @RequestParam(value = "limit", required = false) Integer limit,
                           Model model) {
        model.addAttribute("sectionName", sectionName);
        model.addAttribute("genresName", genresName);
        model.addAttribute("subSectionName", subSectionName);
        model.addAttribute("booksByGenreList", bookService.getPageOfBooksByFolderId(folderId, offset, limit));
        model.addAttribute("folderId", folderId);
        return "genres/slug";
    }

}
