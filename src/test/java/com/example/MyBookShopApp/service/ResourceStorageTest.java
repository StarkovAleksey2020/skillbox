package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.book.file.BookFileEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.BookFileRepository;
import com.example.MyBookShopApp.utils.NullableConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResourceStorageTest {

    private final ResourceStorage resourceStorage;

    @MockBean
    private BookFileRepository bookFileRepositoryMock;

    @Autowired
    public ResourceStorageTest(ResourceStorage resourceStorage) {
        this.resourceStorage = resourceStorage;
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

//    @ParameterizedTest
//    @CsvSource({"/home/alex/data/skillbox"})
//    void getBookFileMime_withValidHash_getPath(String hash) throws BookstoreAPiWrongParameterException {
//        MediaType mockMediaType = mock(MediaType.class);
////        BookFileEntity bookFileEntity = new BookFileEntity();
////
////        bookFileEntity.setPath("/home/alex/data/skillbox");
//        Mockito.doReturn("application/jpg")
//                .when(MediaType.parseMediaType(mockMediaType));
////
////        assertNotNull(resourceStorage.getBookFileMime(hash));
////        assertEquals("/home/alex/data/skillbox", resourceStorage.getBookFileMime(hash).toString());
//    }

}