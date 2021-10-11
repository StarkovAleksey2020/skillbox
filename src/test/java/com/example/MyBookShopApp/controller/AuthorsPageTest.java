package com.example.MyBookShopApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AuthorsPageTest {

    private final MockMvc mockMvc;

    @Autowired
    public AuthorsPageTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void authorsPage() throws Exception {
        mockMvc.perform(get("/authors"))
                .andDo(print())
                .andExpect(content().string(containsString("")))
                .andExpect(status().isOk());
    }

    @Test
    public void slugPage() throws Exception {
        String authorName = "Tatum Gerb";
        String description = "Nullam porttitor lacus at turpis. Donec posuere metus vitae ipsum. Aliquam non mauris. Morbi non lectus. Aliquam sit amet diam in magna bibendum imperdiet. Nullam orci pede, venenatis non, sodales sed, tincidunt eu, felis. Fusce posuere felis sed lacus. Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem. Sed sagittis.";
        String photo = "http://dummyimage.com/252x716.png/dddddd/000000";
        Long authorId = 1L;
        mockMvc.perform(get("/authors/slug")
                .param("authorName", authorName)
                .param("description", description)
                .param("photo", photo)
                .param("authorId", String.valueOf(authorId)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}