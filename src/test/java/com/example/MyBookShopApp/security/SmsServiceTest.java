package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.SmsCode;
import com.example.MyBookShopApp.entity.book.links.Book2RateEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import com.example.MyBookShopApp.repository.SmsCodeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@AutoConfigureMockMvc
class SmsServiceTest {

    private final SmsService smsService;
    private final SmsCodeRepository smsCodeRepository;

    @Autowired
    public SmsServiceTest(SmsService smsService, SmsCodeRepository smsCodeRepository) {
        this.smsService = smsService;
        this.smsCodeRepository = smsCodeRepository;
    }

    @BeforeEach
    void setUp() {
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        SmsCode smsCode = new SmsCode();
        smsCode.setCode("111222");
        smsCode.setExpireTime(LocalDateTime.now().plusSeconds(60));

        smsCodeRepository.save(smsCode);
    }

    @AfterEach
    void tearDown() {
        SmsCode smsCode = smsCodeRepository.findByCode("111222");
        if (smsCode != null) {
            smsCodeRepository.delete(smsCode);
        }
    }

    @Test
    void verifyCode() {
        assertTrue(smsService.verifyCode("111 222"));
    }
}