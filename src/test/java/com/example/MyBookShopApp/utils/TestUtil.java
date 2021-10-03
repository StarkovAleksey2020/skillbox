package com.example.MyBookShopApp.utils;

import com.example.MyBookShopApp.entity.user.UserEntity;
import com.example.MyBookShopApp.security.UserEntityRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestUtil {
    public static UserEntity createUser(String email,
                                        String password,
                                        BCryptPasswordEncoder bCryptPasswordEncoder,
                                        UserEntityRepository testUserRepository) {
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPassword(encryptedPassword);
        userEntity.setRegTime(LocalDateTime.now());
        userEntity.setHash(UUID.randomUUID().toString());
        userEntity.setBalance(100);
        userEntity.setPhone("89011011122");
        userEntity.setName("test9");
        return testUserRepository.save(userEntity);
    }
}
