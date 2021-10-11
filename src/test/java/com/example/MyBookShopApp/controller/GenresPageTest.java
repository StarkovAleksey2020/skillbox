package com.example.MyBookShopApp.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GenresPageTest {

    private final MockMvc mockMvc;

    @Autowired
    public GenresPageTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void genresPage_getOk() throws Exception {
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk());
    }

    @Test
    void slugPage_getOk() throws Exception {
        String sectionName = "Easy reading";
        String genresName = "Thriller Cool detective Ironic detective About maniacs Spy detective Crime detective Classic detective Political detective";
        String subSectionName = "Detectives";
        String genreName = "Thriller";
        Integer offset = 0;
        Integer limit = 10;

        MvcResult result = mockMvc.perform(get("/genres/slug")
                .param("sectionName", sectionName)
                .param("genresName", genresName)
                .param("subSectionName", subSectionName)
                .param("genreName", genreName)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(-1, result.getModelAndView().getModel().get("folderId"));
    }

    @Test
    void slugFolderPage_getOk() throws Exception {
        String sectionName = "Easy reading";
        String genresName = "Thriller Cool detective Ironic detective About maniacs Spy detective Crime detective Classic detective Political detective";
        String subSectionName = "Detectives";
        Long folderId = 5L;

        MvcResult result = mockMvc.perform(get("/genres/folder/slug")
                .param("sectionName", sectionName)
                .param("genresName", genresName)
                .param("subSectionName", subSectionName)
                .param("folderId", String.valueOf(folderId)))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(5L, result.getModelAndView().getModel().get("folderId"));
    }
}