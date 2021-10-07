package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.AuthorRepository;
import com.example.MyBookShopApp.utils.NullableConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class AuthorServiceTest {

    private final AuthorService authorService;

    private static List<AuthorEntity> TEST_AUTHOR_LIST = new ArrayList<>();

    @MockBean
    private AuthorRepository authorRepositoryMock;

    @Autowired
    public AuthorServiceTest(AuthorService authorService) {
        this.authorService = authorService;
    }

    @BeforeAll
    static void beforeAll() {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(1L);
        authorEntity.setName("Alan Acht");
        authorEntity.setDescription("aaaaa");
        TEST_AUTHOR_LIST.add(authorEntity);
        AuthorEntity authorEntity1 = new AuthorEntity();
        authorEntity1.setId(1L);
        authorEntity1.setName("Bill Exzenter");
        authorEntity1.setDescription("bbbbb");
        TEST_AUTHOR_LIST.add(authorEntity1);
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void getDescriptionVisible_withNullDescription_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String description) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> authorService.getDescriptionVisible(description));
    }

    @ParameterizedTest
    @CsvSource({"One. Two. Three. Four."})
    void getDescriptionVisible_withValidDescription_getDescriptionVisible(String description) throws BookstoreAPiWrongParameterException {
        assertEquals(2, authorService.getDescriptionVisible(description).split("\\.").length);
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void getDescriptionHidden_withNullDescription_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String description) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> authorService.getDescriptionHidden(description));
    }

    @ParameterizedTest
    @CsvSource({"One. Two. Three. Four."})
    void getDescriptionHidden_withValidDescription_getDescriptionHidden(String description) throws BookstoreAPiWrongParameterException {
        assertEquals(2, authorService.getDescriptionHidden(description).split("\\.").length - 1);
    }

    @Test
    void createAuthorsHashMap_getAuthorsHashMap() {
        Mockito.when(authorRepositoryMock.findAll()).thenReturn(TEST_AUTHOR_LIST);
        assertEquals("B", authorService.createAuthorsHashMap().keySet().toArray()[1].toString());
    }
}