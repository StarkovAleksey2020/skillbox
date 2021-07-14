package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GenresPageController {

    @GetMapping("/genres")
    public String genresPage() {
        return "genres/index";
    }

    @GetMapping("/genres/slug")
    public String slugPage(@RequestParam(value = "sectionName", required = false) String sectionName,
                           @RequestParam(value = "genresName", required = false) String genresName,
                           @RequestParam(value = "subSectionName", required = false) String subSectionName,
                           Model model) {
        model.addAttribute("sectionName", sectionName);
        model.addAttribute("genresName", genresName);
        model.addAttribute("subSectionName", subSectionName);
        return "genres/slug";
    }

}
