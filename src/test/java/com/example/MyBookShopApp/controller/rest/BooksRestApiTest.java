package com.example.MyBookShopApp.controller.rest;

import com.example.MyBookShopApp.data.SearchWordDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BooksRestApiTest {

    private final MockMvc mockMvc;

    @Autowired
    public BooksRestApiTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void booksByAuthor_getOk() throws Exception {
        String author = "Tatum Gerb";

        MvcResult result = mockMvc.perform(get("/api/books/by-author")
                .param("author", author))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Randy and the Mob"));
    }

    @Test
    void booksByAuthor_get400() throws Exception {
        String author = "wrong value";

        MvcResult result = mockMvc.perform(get("/api/books/by-author")
                .param("author", author))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void booksByTitle_getOk() throws Exception {
        String title = "Randy and the Mob";

        MvcResult result = mockMvc.perform(get("/api/books/by-title")
                .param("title", title))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("https://www.litmir.me/"));
        assertTrue(result.getResponse().getContentAsString().contains(title));
    }

    @Test
    void getPriceRangeBooks_getOk() throws Exception {
        Integer min = 10;
        Integer max = 10000;

        MvcResult result = mockMvc.perform(get("/api/books/by-price-range")
                .param("min", String.valueOf(min))
                .param("max", String.valueOf(max)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getMaxDiscountBooks_getOk() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/books/with-max-discount"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getBestsellers_getOk() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/books/bestsellers"))
                .andExpect(status().isOk())
                .andReturn();
    }
}