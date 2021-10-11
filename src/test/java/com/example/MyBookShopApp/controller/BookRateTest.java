package com.example.MyBookShopApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookRateTest {

    private final MockMvc mockMvc;

    @Autowired
    public BookRateTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void handleBookRate() throws Exception {
        String slug = "lNv3IiN";
        Integer rate = 5;

        mockMvc.perform(post("/rateBook/" + slug + "/" + rate))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

    }
}