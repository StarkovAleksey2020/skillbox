package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.repository.BookFileRepository;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.repository.UserRepository;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.ResourceStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private BookFileRepository bookFileRepositoryMock;

    @MockBean
    private ResourceStorage resourceStorageMock;

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    public BookControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    static void beforeAll() {

    }

    @Test
    void getBookPage() throws Exception {
        String slug = "lNv3IiN";
        mockMvc.perform(get("/books/" + slug))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getPostponedSize() throws Exception {
        BookService mockBookService = mock(BookService.class);
        when(mockBookService.getCartCount(Mockito.any())).thenReturn(5);
        mockMvc.perform(get("/")
                .flashAttr("postponedSize", 5));

    }

    @Test
    void getCartContentsSize() throws Exception {
        BookService mockBookService = mock(BookService.class);
        when(mockBookService.getCartCount(Mockito.any())).thenReturn(3);
        mockMvc.perform(get("/")
                .flashAttr("cartContentsSize", 3));
    }

    @Test
    void saveNewBookImage() throws Exception {
        BookEntity mockBookEntity = mock(BookEntity.class);

        BookService mockBookService = mock(BookService.class);

        BookRepository mockBookRepository = mock(BookRepository.class);

        when(mockBookService.checkCredentials(Mockito.any())).thenReturn(true);

        Mockito.doReturn(mockBookEntity)
                .when(mockBookRepository)
                .save(Mockito.any());

        String inputString = "Hello World!";
        String charsetName = "IBM01140";
        byte[] byteArrray = inputString.getBytes("IBM01140");

        String slug = "any-slug";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/books/" + slug + "/img/save")
                .file("file", byteArrray))
                .andReturn().getResponse().getStatus();
    }

    @Test
    void getBookFile() throws Exception {
        String inputString = "Hello World!";
        String charsetName = "IBM01140";
        byte[] byteArrray = inputString.getBytes("IBM01140");

        String hash = "oWwgqobmXW";

        when(resourceStorageMock.getBookFilePath(Mockito.any())).thenReturn(Paths.get("Undocumented.pdf"));
        when(resourceStorageMock.getBookFileMime(Mockito.any())).thenReturn(MediaType.parseMediaType("application/json;charset=UTF-8"));
        when(resourceStorageMock.getBookFileByteArray(Mockito.any())).thenReturn(byteArrray);

        mockMvc.perform(get("/books/download/" + hash))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void handleLikesDislikes() throws Exception {
        String slug = "1OoxBIQoQY3";
        Long reviewId = 7L;
        Long value = 0L;

        mockMvc.perform(post("/books/rateBookReview")
                .param("slug", slug)
                .param("reviewid", String.valueOf(reviewId))
                .param("value", String.valueOf(value)))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}