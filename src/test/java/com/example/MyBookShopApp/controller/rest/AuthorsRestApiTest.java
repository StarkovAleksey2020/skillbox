package com.example.MyBookShopApp.controller.rest;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthorsRestApiTest {

    private final MockMvc mockMvc;

    @Autowired
    public AuthorsRestApiTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void getAuthorsHashMap() throws Exception {
        MvcResult result = mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getModelAndView().getModel().get("authorsMap") instanceof HashMap);
    }
}