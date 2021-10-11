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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookPaginationRestApiTest {

    private final MockMvc mockMvc;

    @Autowired
    public BookPaginationRestApiTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void getBooksPage_getOk() throws Exception {
        Integer offset = 0;
        Integer limit = 10;

        mockMvc.perform(get("/books/recommended")
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk());
    }

    @Test
    void getBooksRecentPage_getOk() throws Exception {
        Integer offset = 0;
        Integer limit = 10;

        mockMvc.perform(get("/books/recent")
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk());
    }


    @Test
    void getBooksTagsPage_getOk() throws Exception {
        String tagName = "fiction";
        Integer offset = 0;
        Integer limit = 10;

        MvcResult result = mockMvc.perform(get("/books/recent")
                .param("tagName", tagName)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("count"));
    }

    @Test
    void getBooksPageByGenre_getOk() throws Exception {
        String genreName = "Fantasy";
        Integer offset = 0;
        Integer limit = 10;

        MvcResult result = mockMvc.perform(get("/books/genre")
                .param("genreName", genreName)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("count"));
    }

    @Test
    void getBooksPageByAuthor_getOk() throws Exception {
        String authorName = "Tatum Gerb";
        Integer offset = 0;
        Integer limit = 10;

        MvcResult result = mockMvc.perform(get("/books/author")
                .param("authorName", authorName)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("count"));
    }


    @Test
    void getBooksPageByGenreFolder_getOk() throws Exception {
        Long folderId = 1L;
        Integer offset = 0;
        Integer limit = 10;

        MvcResult result = mockMvc.perform(get("/books/folder/genre")
                .param("folderId", String.valueOf(folderId))
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("count"));
    }

    @Test
    void getBooksRecentPageInDateInterval_getOk() throws Exception {
        String from = "01.01.2000";
        String to = "01.01.2022";
        Integer offset = 0;
        Integer limit = 10;

        MvcResult result = mockMvc.perform(get("/books/recent/interval")
                .param("from", from)
                .param("to", to)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("count"));
    }

    @Test
    void getBooksPopularPage_getOk() throws Exception {
        Integer offset = 0;
        Integer limit = 10;

        MvcResult result = mockMvc.perform(get("/books/popular")
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("count"));
    }

    @Test
    void getSearchResults_getOk() throws Exception {
        SearchWordDto searchWordDto = new SearchWordDto();
        searchWordDto.setExample("Test search");

        MvcResult result = mockMvc.perform(get("/search/" + searchWordDto))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getModelAndView().getModel().get("searchWordDto") instanceof SearchWordDto);
    }

    @Test
    void getNextSearchPage_getOk() throws Exception {
        SearchWordDto searchWordDto = new SearchWordDto();
        searchWordDto.setExample("Batman");

        Integer offset = 0;
        Integer limit = 10;

        MvcResult result = mockMvc.perform(get("/search/" + searchWordDto)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getModelAndView().getModel().get("searchWordDto") instanceof SearchWordDto);
    }
}