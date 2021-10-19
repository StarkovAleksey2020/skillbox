package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookShopPostponedTest {

    private final MockMvc mockMvc;

    @MockBean
    private BookService bookServiceMock;

    @MockBean
    private SecurityContextHolder securityContextHolderMock;

    @Autowired
    public BookShopPostponedTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handlePostponedRequest_withNotNullPrincipalAndEmptyPostponed_getEmptyPostponed() throws Exception {
        Mockito.when(bookServiceMock.getPostponedCount(Mockito.any())).thenReturn(0);
        MvcResult result = mockMvc.perform(get("/books/postponed")
                .accept(MediaType.ALL))
                .andReturn();
        assertEquals("true", result.getModelAndView().getModel().get("isPostponedEmpty").toString());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handlePostponedRequest_withNotNullPrincipalAndNotEmptyPostponed_getNotEmptyPostponed() throws Exception {
        Mockito.when(bookServiceMock.getPostponedCount(Mockito.any())).thenReturn(3);
        MvcResult result = mockMvc.perform(get("/books/postponed")
                .accept(MediaType.ALL))
                .andReturn();
        assertEquals("false", result.getModelAndView().getModel().get("isPostponedEmpty").toString());
    }

    @Test
    void handlePostponedRequest_withNullPrincipalAndEmptyCookie_getEmptyPostponed() throws Exception {
        Mockito.when(bookServiceMock.getPostponedCountTempUser(Mockito.any())).thenReturn(0);
        MvcResult result = mockMvc.perform(get("/books/postponed"))
                .andReturn();

        assertEquals("true", result.getModelAndView().getModel().get("isPostponedEmpty").toString());
    }

    @Test
    void handlePostponedRequest_withNullPrincipalAndNotEmptyCookie_getNotEmptyPostponed() throws Exception {
        Mockito.when(bookServiceMock.getPostponedCountTempUser(Mockito.any())).thenReturn(10);
        MvcResult result = mockMvc.perform(get("/books/postponed"))
                .andReturn();

        assertEquals("false", result.getModelAndView().getModel().get("isPostponedEmpty").toString());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handleRemoveBookFromPostponedRequest_withNotNullPrincipalAndNotNullSlug_getRedirected() throws Exception {
        String slug = "bbbb";

        mockMvc.perform(post("/books/changeBookStatus/postponed/remove/" + slug))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handleBuyBookFromPostponedRequest_get302() throws Exception {
        String slug = "aaaa";
        mockMvc.perform(post("/books/changeBookStatus/postponed/buy/" + slug))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void handleBuyBookFromPostponedRequest_withNullPrincipalAndNotEmptyCookie_getRedistributedCartCookie() throws Exception {
        Cookie cookiePostponed = new Cookie("cartContents", "/aaaa/bbbb");
        Cookie cookieCart = new Cookie("postponedContents", "/cccc");
        Cookie mockCookie = mock(Cookie.class);
        Mockito.when(mockCookie.getName()).thenReturn("cartContents");

        Mockito.when(bookServiceMock.removePostponedItemTempUser(Mockito.any(), Mockito.any())).thenReturn(new Cookie("postponedContents", "/aaaa"));
        Mockito.when(bookServiceMock.addCartItemTempUser(Mockito.any(), Mockito.any())).thenReturn(new Cookie("cartContents", "/cccc/bbbb"));

        String slug = "bbbb";

        MvcResult result = mockMvc.perform(post("/books/changeBookStatus/postponed/buy/" + slug)
                .cookie(cookieCart)
                .cookie(cookiePostponed)
                .accept(MediaType.ALL))
                .andReturn();

        assertEquals("/cccc/bbbb", result.getResponse().getCookie("cartContents").getValue());
        assertEquals("/aaaa", result.getResponse().getCookie("postponedContents").getValue());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handleChangeBookStatus_withNotNullPrincipalAndNotEmptySlug_get302() throws Exception {
        String slug = "aaaa";
        mockMvc.perform(post("/books/changeBookStatus/postponed/" + slug)
                .accept(MediaType.ALL))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void handleChangeBookStatus_withNullPrincipalAndNotEmptyCookie_getAddedCartCookie() throws Exception {
        Cookie cookiePostponed = new Cookie("postponedContents", "/aaaa");
        Cookie mockCookie = mock(Cookie.class);
        Mockito.when(mockCookie.getName()).thenReturn("postponedContents");

        Mockito.when(bookServiceMock.addPostponedItemTempUser(Mockito.any(), Mockito.any())).thenReturn(new Cookie("postponedContents", "/aaaa/bbbb"));

        String slug = "bbbb";

        MvcResult result = mockMvc.perform(post("/books/changeBookStatus/postponed/" + slug)
                .cookie(cookiePostponed)
                .accept(MediaType.ALL))
                .andReturn();

        assertEquals("/aaaa/bbbb", result.getResponse().getCookie("postponedContents").getValue());
    }
}