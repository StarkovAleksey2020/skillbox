package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.user.UserEntity;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserEntityRegisterTest {

    private final UserEntityRegister userEntityRegister;
    private final PasswordEncoder passwordEncoder;
    private RegistrationForm registrationForm;

    @MockBean
    private UserEntityRepository userEntityRepositoryMock;

    @Autowired
    public UserEntityRegisterTest(UserEntityRegister userEntityRegister, PasswordEncoder passwordEncoder) {
        this.userEntityRegister = userEntityRegister;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.org");
        registrationForm.setName("Tester");
        registrationForm.setPassword("iddqd");
        registrationForm.setPhone("9011234567");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
    }

    @Test
    void registerNewUser() {
        UserEntity userEntity = userEntityRegister.registerNewUser(registrationForm);
        assertNotNull(userEntity);
        assertTrue(passwordEncoder.matches(registrationForm.getPassword(), userEntity.getPassword()));
        assertTrue(CoreMatchers.is(userEntity.getPhone()).matches(registrationForm.getPhone()));
        assertTrue(CoreMatchers.is(userEntity.getEmail()).matches(registrationForm.getEmail()));
        assertTrue(CoreMatchers.is(userEntity.getName()).matches(registrationForm.getName()));

        Mockito.verify(userEntityRepositoryMock, Mockito.times(1))
                .save((Mockito.any(UserEntity.class)));
    }

    @Test
    void registerNewUserFail() {
        Mockito.doReturn(new UserEntity())
                .when(userEntityRepositoryMock)
                .findUserEntityByEmail(registrationForm.getEmail());

        UserEntity userEntity = userEntityRegister.registerNewUser(registrationForm);
        assertNull(userEntity);
    }
}