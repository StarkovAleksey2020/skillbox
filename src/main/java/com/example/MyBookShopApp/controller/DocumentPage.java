package com.example.MyBookShopApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentPage {

    @GetMapping("/documents")
    public String getDocumentsPage() {
        return "documents/index";
    }

}
