package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.user.UserEntity;
import com.example.MyBookShopApp.utils.TestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserEntityRegisterTest {

    private final UserEntityRegister userEntityRegister;
    private final PasswordEncoder passwordEncoder;
    private RegistrationForm registrationForm;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserEntityRepository userEntityRepository;

    private String token;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private UserEntityRepository userEntityRepositoryMock;

    @Autowired
    public UserEntityRegisterTest(UserEntityRegister userEntityRegister, PasswordEncoder passwordEncoder, BCryptPasswordEncoder bCryptPasswordEncoder, UserEntityRepository userEntityRepository) {
        this.userEntityRegister = userEntityRegister;
        this.passwordEncoder = passwordEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userEntityRepository = userEntityRepository;
    }

    @BeforeEach
    void setUp() throws JsonProcessingException {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.org");
        registrationForm.setName("Tester");
        registrationForm.setPassword("iddqd");
        registrationForm.setPhone("9011234567");

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        token = getToken();
    }

    @AfterEach
    void tearDown() {
        UserEntity userEntity = userEntityRepository.findUserEntityByEmail("test9@example.com");
        if (userEntity != null) {
            userEntityRepository.delete(userEntity);
        }
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

        Mockito.verify(userEntityRepositoryMock, Mockito.times(2))
                .save((Mockito.any(UserEntity.class)));
    }

    @Test
    void registerNewUserFail() {
        Mockito.doReturn(new UserEntity())
                .when(userEntityRepositoryMock)
                .findUserEntityByEmail(registrationForm.getEmail());

        UserEntity userEntity = userEntityRegister.registerNewUser(registrationForm);
        assertNotNull(userEntity);
    }

    @Test
    public void testSignIn() {
        String email = "test9@example.com";
        String password = "1234567";
        createTestUser(email, password);
        String url = "https://localhost:8085/login";
        HttpHeaders headers = new HttpHeaders();

        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact(email);
        payload.setCode(password);
        HttpEntity<ContactConfirmationPayload> entity = new HttpEntity<>(payload, headers);

        // act
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // verify
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    private void createTestUser(String email, String password) {
        userEntityRepositoryMock.save(TestUtil.createUser(email, password, bCryptPasswordEncoder, userEntityRepositoryMock));
    }

    private String getToken() throws JsonProcessingException {
        String email = "test9@example.com";
        String password = "1234567";
        createTestUser(email, password);
        String url = "https://localhost:8085/login";
        HttpHeaders headers = new HttpHeaders();

        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact(email);
        payload.setCode(password);
        HttpEntity<ContactConfirmationPayload> entity = new HttpEntity<>(payload, headers);

        // act
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readTree(result.getBody()).get("result").toString();
    }
}