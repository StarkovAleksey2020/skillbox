package com.example.MyBookShopApp.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MainPageTest {

    private final MockMvc mockMvc;

    @Autowired
    public MainPageTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void getPopularBooksPage_getOk() throws Exception {
        mockMvc.perform(get("/popular"))
                .andExpect(status().isOk());
    }

    @Test
    void getRecentBooksPage_getOk() throws Exception {
        mockMvc.perform(get("/recent"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookInfo_getOk() throws Exception {
        String bookTitle = "Test book title";
        String bookAuthorName = "Test author name";
        String bookPriceOld = "Test price old";
        String bookPrice = "Test price current";

        mockMvc.perform(get("/slug")
                .param("bookTitle", bookTitle)
                .param("bookAuthorName", bookAuthorName)
                .param("bookPriceOld", bookPriceOld)
                .param("bookPrice", bookPrice))
                .andExpect(status().isOk());
    }

    @Test
    void getPostponedBooks_getOk() throws Exception {
        mockMvc.perform(get("/postponed"))
                .andExpect(status().isOk());
    }

    @Test
    void getCart_getOk() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk());
    }

    @Test
    void getAboutPage_getOk() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk());
    }

    @Test
    void getContactsPage_getOk() throws Exception {
        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk());
    }

    @Test
    void getFAQPage_getOk() throws Exception {
        mockMvc.perform(get("/faq"))
                .andExpect(status().isOk());
    }
}