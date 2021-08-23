package com.example.MyBookShopApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchPageController {

    @GetMapping("/search")
    public String getSearchPage() {
        return "search/index";
    }

}
