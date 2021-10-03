package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.utils.NullableConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorServiceTest {

    private final AuthorService authorService;

    @Autowired
    public AuthorServiceTest(AuthorService authorService) {
        this.authorService = authorService;
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
}