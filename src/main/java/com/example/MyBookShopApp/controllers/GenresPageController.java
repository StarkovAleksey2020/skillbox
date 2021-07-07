package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class GenresPageController {

    @GetMapping("/genres")
    public String genresPage() {
        return "genres";
    }

    @GetMapping("/genres/slug")
    public String slugPage(@RequestParam(value = "genresName", required = false) String genresName,
                           Model model) {
        model.addAttribute("genresName", genresName);
        return "slug_genres";
    }

}
