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
class TagsPageTest {

    private final MockMvc mockMvc;

    @Autowired
    public TagsPageTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void getTagsPage_getOk() throws Exception {
        String tagName = "Test tag name";
        Integer offset = 0;
        Integer limit = 10;

        mockMvc.perform(get("/tags")
                .param("tagName", tagName)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk());
    }

    @Test
    void getBookInfo_getOk() throws Exception {
        String bookTitle = "Test book title";
        String bookAuthorName = "Test author name";
        String description = "Test description";
        Long id = 1L;
        String price = "100";
        String bookImage = "/home/data/coverage";
        Integer isBestseller = 1;
        String bookDiscount = "12";

        mockMvc.perform(get("/tags/slug")
                .param("bookTitle", bookTitle)
                .param("bookAuthorName", bookAuthorName)
                .param("description", description)
                .param("id", String.valueOf(id))
                .param("price", price)
                .param("bookImage", bookImage)
                .param("isBestseller", String.valueOf(isBestseller))
                .param("bookDiscount", bookDiscount))
                .andExpect(status().isOk());

    }
}