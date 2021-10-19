package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.user.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class UserEntityRegisterRegsTest {

    private final UserEntityRegister userEntityRegister;
    private final PasswordEncoder passwordEncoder;
    private RegistrationForm registrationForm;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserEntityRepository userEntityRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private UserEntityRepository userEntityRepositoryMock;

    @Autowired
    public UserEntityRegisterRegsTest(UserEntityRegister userEntityRegister, PasswordEncoder passwordEncoder, BCryptPasswordEncoder bCryptPasswordEncoder, UserEntityRepository userEntityRepository, AuthenticationManager authenticationManager) {
        this.userEntityRegister = userEntityRegister;
        this.passwordEncoder = passwordEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userEntityRepository = userEntityRepository;
        this.authenticationManager = authenticationManager;
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

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
        UserEntity user = new UserEntity();
        user.setEmail("test@mail.org");
        user.setName("Tester");
        user.setPassword("iddqd");
        user.setPhone("9011234567");

        UserEntityRegister mockUserEntityRegister = mock(UserEntityRegister.class);

        Mockito.when(userEntityRepositoryMock.findUserEntityByEmail(Mockito.any())).thenReturn(user);
        Mockito.when(userEntityRepositoryMock.findUserEntityByPhone(Mockito.any())).thenReturn(user);
        Mockito.when(mockUserEntityRegister.registerNewUser(Mockito.any())).thenReturn(user);

        UserEntity userEntity = userEntityRegister.registerNewUser(registrationForm);
        assertNotNull(userEntity);
        assertEquals(registrationForm.getPassword(), userEntity.getPassword());
        assertTrue(CoreMatchers.is(userEntity.getPhone()).matches(registrationForm.getPhone()));
        assertTrue(CoreMatchers.is(userEntity.getEmail()).matches(registrationForm.getEmail()));
        assertTrue(CoreMatchers.is(userEntity.getName()).matches(registrationForm.getName()));
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
