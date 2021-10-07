package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.book.file.BookFileEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.BookFileRepository;
import com.example.MyBookShopApp.utils.NullableConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ResourceStorageTest {

    private final ResourceStorage resourceStorage;

    private static BookFileEntity TEST_BOOK_FILE_ENTITY = new BookFileEntity();
    private static final MediaType JSON_UTF8 = MediaType.parseMediaType("application/json;charset=UTF-8");

    @MockBean
    private BookFileRepository bookFileRepositoryMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    public ResourceStorageTest(ResourceStorage resourceStorage) {
        this.resourceStorage = resourceStorage;
    }

    @BeforeAll
    static void beforeAll() {
        TEST_BOOK_FILE_ENTITY.setPath("/home/test/book-name.pdf");
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void getBookFilePath_withNullHash_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String hash) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> resourceStorage.getBookFilePath(hash));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void getBookFilePath_withEmptyHash_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String hash) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> resourceStorage.getBookFilePath(hash));
    }

    @ParameterizedTest
    @CsvSource({"/home/alex/data/skillbox"})
    void getBookFilePath_withValidHash_getPath(String hash) throws BookstoreAPiWrongParameterException {
        BookFileEntity bookFileEntity = new BookFileEntity();

        bookFileEntity.setPath("/home/alex/data/skillbox");
        Mockito.doReturn(bookFileEntity)
                .when(bookFileRepositoryMock)
                .findBookFileEntityByHash(hash);

        assertNotNull(resourceStorage.getBookFilePath(hash));
        assertEquals("/home/alex/data/skillbox", resourceStorage.getBookFilePath(hash).toString());
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void getBookFileMime_withNullHash_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String hash) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> resourceStorage.getBookFileMime(hash));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void getBookFileMime_withEmptyHash_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String hash) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> resourceStorage.getBookFileMime(hash));
    }

    @Test
    void getMimeType_withMockBookFileEntity_getStringMimeType() {
        BookFileEntity mockBookFileEntity = mock(BookFileEntity.class);
        when(mockBookFileEntity.getPath()).thenReturn("/home/data");

        ResourceStorage mockResourceStorage = mock(ResourceStorage.class);
        when(mockResourceStorage.getMimeType(any())).thenReturn("application/json;charset=UTF-8");

        assertEquals("application/json;charset=UTF-8", mockResourceStorage.getMimeType(mockBookFileEntity));
    }

    @Test
    public void getBookFileMime_withMockHash_getMediaType() throws BookstoreAPiWrongParameterException {
        MediaType mockMediaType = mock(MediaType.class);
        ResourceStorage mockResourceStorage = mock(ResourceStorage.class);
        when(mockResourceStorage.getBookFileMime(any())).thenReturn(mockMediaType);
        assertNotNull(mockResourceStorage.getBookFileMime("hash-string"));
    }


    @ParameterizedTest
    @ValueSource(strings = {"aaaa"})
    void saveNewBookImage_withMockFileUpload_getString(String slug) throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World! (From E.Baeldung)".getBytes()
        );

        ResourceStorage mockResourceStorage = mock(ResourceStorage.class);
        when(mockResourceStorage.saveNewBookImage(any(), any())).thenReturn("/book-covers/" + slug + ".txt");
    }

    @ParameterizedTest
    @CsvSource({"null"})
    void getBookFileByteArray_withNullHash_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String hash) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> resourceStorage.getBookFileByteArray(hash));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void getBookFileByteArray_withEmptyHash_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String hash) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> resourceStorage.getBookFileByteArray(hash));
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaaa"})
    void getBookFileByteArray_withValidHash_getByteArray(String hash) throws BookstoreAPiWrongParameterException, IOException {
        String inputString = "Hello World!";
        String charsetName = "IBM01140";
        byte[] byteArrray = inputString.getBytes("IBM01140");

        ResourceStorage mockResourceStorage = mock(ResourceStorage.class);
        when(mockResourceStorage.getBookFileByteArray(any())).thenReturn(byteArrray);
        assertArrayEquals(
                new byte[]{-56, -123, -109, -109, -106, 64, -26,
                        -106, -103, -109, -124, 90},
                mockResourceStorage.getBookFileByteArray(hash));
    }

}