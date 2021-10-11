package com.example.MyBookShopApp.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DocumentPageTest {

    private final MockMvc mockMvc;

    @Autowired
    public DocumentPageTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void getDocumentsPage_getOk() throws Exception {
        mockMvc.perform(get("/documents"))
                .andExpect(status().isOk());
    }
}