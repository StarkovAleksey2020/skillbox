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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookShopCartTest {

    private final MockMvc mockMvc;

    @MockBean
    private BookService bookServiceMock;

    @MockBean
    private SecurityContextHolder securityContextHolderMock;

    @Autowired
    public BookShopCartTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void handleCartRequest_withNullPrincipalAndEmptyCookie_getEmptyCart() throws Exception {
        Mockito.when(bookServiceMock.getCartCountTempUser(Mockito.any())).thenReturn(0);
        MvcResult result = mockMvc.perform(get("/books/cart"))
                .andReturn();

        assertEquals("true", result.getModelAndView().getModel().get("isCartEmpty").toString());
    }

    @Test
    void handleCartRequest_withNullPrincipalAndNotEmptyCookie_getNotEmptyCart() throws Exception {
        Mockito.when(bookServiceMock.getCartCountTempUser(Mockito.any())).thenReturn(10);
        MvcResult result = mockMvc.perform(get("/books/cart")
                .flashAttr("isCartEmpty", false))
                .andReturn();

        assertEquals("false", result.getModelAndView().getModel().get("isCartEmpty").toString());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handleCartRequest_withNotNullPrincipalAndNullCart_getEmptyCart() throws Exception {
        Mockito.when(bookServiceMock.getCartCount(Mockito.any())).thenReturn(0);
        MvcResult result = mockMvc.perform(get("/books/cart")
                .accept(MediaType.ALL))
                .andReturn();
        assertEquals("true", result.getModelAndView().getModel().get("isCartEmpty").toString());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handleCartRequest_withNotNullPrincipalAndNotNullCart_getNotEmptyCart() throws Exception {
        Mockito.when(bookServiceMock.getCartCount(Mockito.any())).thenReturn(3);
        MvcResult result = mockMvc.perform(get("/books/cart")
                .accept(MediaType.ALL))
                .andReturn();
        assertEquals("false", result.getModelAndView().getModel().get("isCartEmpty").toString());
    }

    @Test
    void handleRemoveBookFromCartRequest_withNullPrincipalAndNotEmptyCookie_getReducedCartCookie() throws Exception {
        Cookie cookie = new Cookie("cartContents", "/aaaa/bbbb");
        Cookie mockCookie = mock(Cookie.class);
        Mockito.when(mockCookie.getName()).thenReturn("cartContents");

        Mockito.when(bookServiceMock.removeCartItemTempUser(Mockito.any(), Mockito.any())).thenReturn(new Cookie("cartContents", "/aaaa"));
        String slug = "bbbb";
        MvcResult result = mockMvc.perform(post("/books/changeBookStatus/cart/remove/" + slug)
                .cookie(cookie)
                .accept(MediaType.ALL))
                .andReturn();
        assertEquals("/aaaa", result.getResponse().getCookie("cartContents").getValue());
    }

    @Test
    void handleRemoveBookFromCartRequest_withNullPrincipalAndNullSlug_get404() throws Exception {
        String slug = "";
        mockMvc.perform(post("/books/changeBookStatus/cart/remove/" + slug)
                .accept(MediaType.ALL))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handleRemoveBookFromCartRequest_withNotNullPrincipalAndNotNullSlug_getRedirected() throws Exception {

        String slug = "bbbb";

        mockMvc.perform(post("/books/changeBookStatus/cart/remove/" + slug)
                .accept(MediaType.ALL))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handleMoveBookFromCartToKeptRequest_withNotNullPrincipalAndEmptySlug_get404() throws Exception {
        String slug = "";
        mockMvc.perform(post("/books/changeBookStatus/cart/kept/" + slug)
                .accept(MediaType.ALL))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handleMoveBookFromCartToKeptRequest_withNotNullPrincipalAndNotEmptySlug_get302() throws Exception {
        String slug = "aaaa";
        mockMvc.perform(post("/books/changeBookStatus/cart/kept/" + slug)
                .accept(MediaType.ALL))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void handleMoveBookFromCartToKeptRequest_withNullPrincipalAndNotEmptyCookie_getRedistributedCartCookie() throws Exception {
        Cookie cookieCart = new Cookie("cartContents", "/aaaa/bbbb");
        Cookie cookiePostponed = new Cookie("postponedContents", "/cccc");
        Cookie mockCookie = mock(Cookie.class);
        Mockito.when(mockCookie.getName()).thenReturn("cartContents");

        Mockito.when(bookServiceMock.removeCartItemTempUser(Mockito.any(), Mockito.any())).thenReturn(new Cookie("cartContents", "/aaaa"));
        Mockito.when(bookServiceMock.addPostponedItemTempUser(Mockito.any(), Mockito.any())).thenReturn(new Cookie("postponedContents", "/cccc/bbbb"));

        String slug = "bbbb";

        MvcResult result = mockMvc.perform(post("/books/changeBookStatus/cart/kept/" + slug)
                .cookie(cookieCart)
                .cookie(cookiePostponed)
                .accept(MediaType.ALL))
                .andReturn();

        assertEquals("/aaaa", result.getResponse().getCookie("cartContents").getValue());
        assertEquals("/cccc/bbbb", result.getResponse().getCookie("postponedContents").getValue());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handleChangeBookStatus_withNotNullPrincipalAndEmptySlug_get404() throws Exception {
        String slug = "";
        mockMvc.perform(post("/books/changeBookStatus/" + slug)
                .accept(MediaType.ALL))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails("test1@example.com")
    void handleChangeBookStatus_withNotNullPrincipalAndNotEmptySlug_get302() throws Exception {
        String slug = "aaaa";
        mockMvc.perform(post("/books/changeBookStatus/" + slug)
                .accept(MediaType.ALL))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void handleChangeBookStatus_withNullPrincipalAndNotEmptyCookie_getAddedCartCookie() throws Exception {
        Cookie cookieCart = new Cookie("cartContents", "/aaaa");
        Cookie mockCookie = mock(Cookie.class);
        Mockito.when(mockCookie.getName()).thenReturn("cartContents");

        Mockito.when(bookServiceMock.addCartItemTempUser(Mockito.any(), Mockito.any())).thenReturn(new Cookie("cartContents", "/aaaa/bbbb"));

        String slug = "bbbb";

        MvcResult result = mockMvc.perform(post("/books/changeBookStatus/" + slug)
                .cookie(cookieCart)
                .accept(MediaType.ALL))
                .andReturn();

        assertEquals("/aaaa/bbbb", result.getResponse().getCookie("cartContents").getValue());
    }
}