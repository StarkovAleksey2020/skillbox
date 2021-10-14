package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserRepositoryTest {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @AfterEach
    void tearDown() {
        UserEntity userEntity = userRepository.findByEmail("test9@example.com");
        if (userEntity != null) {
            userRepository.delete(userEntity);
        }
    }

    @Test
    public void testAddNewUser(){
        String encryptedPassword = bCryptPasswordEncoder.encode("1234567");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test9@example.com");
        userEntity.setPassword(encryptedPassword);
        userEntity.setRegTime(LocalDateTime.now());
        userEntity.setHash(UUID.randomUUID().toString());
        userEntity.setBalance(100);
        userEntity.setPhone("89011011122");
        userEntity.setName("test9");

        assertNotNull(userRepository.save(userEntity));
    }
}