package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.user.UserEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.UserRepository;
import com.example.MyBookShopApp.utils.NullableConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@SpringBootTest
class UserServiceTest {

    private UserService userService;

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void isUserFound_withEmptyEmail_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String email) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> userService.isUserFound(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"null"})
    void isUserFound_withNullEmail_getBookstoreAPiWrongParameterException(@ConvertWith(NullableConverter.class) String email) {
        assertThrows(BookstoreAPiWrongParameterException.class, () -> userService.isUserFound(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com"})
    void isUserFound_withValidEmail_getTrue(String email) throws BookstoreAPiWrongParameterException {
        UserEntity mockUserEntity = mock(UserEntity.class);
        Mockito.when(userRepositoryMock.findByEmail(Mockito.any())).thenReturn(mockUserEntity);
        assertEquals(true, userService.isUserFound(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com"})
    void isUserFound_withInvalidEmail_getFalse(String email) throws BookstoreAPiWrongParameterException {
        Mockito.when(userRepositoryMock.findByEmail(Mockito.any())).thenReturn(null);
        assertEquals(false, userService.isUserFound(email));
    }
}