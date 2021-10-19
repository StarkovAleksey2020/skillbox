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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@SpringBootTest
class UserEntityRegisterTest {

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

    @MockBean
    private UserEntityRegister userEntityRegisterMock;

    @Autowired
    public UserEntityRegisterTest(UserEntityRegister userEntityRegister, PasswordEncoder passwordEncoder, BCryptPasswordEncoder bCryptPasswordEncoder, UserEntityRepository userEntityRepository, AuthenticationManager authenticationManager) {
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

    private void createTestUser(String email, String password) {
        userEntityRepositoryMock.save(TestUtil.createUser(email, password, bCryptPasswordEncoder, userEntityRepositoryMock));
    }

    @Test
    void testGetCurrentUser() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("test9@example.com");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("https://localhost:8085/signin")
                .principal(mockPrincipal)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        assertEquals(200, status);
    }

    @Test
    void jwtLogin_getNotNull() {
        ContactConfirmationResponse mockContactConfirmationResponse = mock(ContactConfirmationResponse.class);
        HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        ContactConfirmationPayload mockContactConfirmationPayload = mock(ContactConfirmationPayload.class);
        Mockito.when(userEntityRegisterMock.jwtLogin(any(), any())).thenReturn(mockContactConfirmationResponse);

        assertNotNull(userEntityRegister.jwtLogin(mockHttpServletRequest, mockContactConfirmationPayload));
    }

    @Test
    void login() {
        ContactConfirmationResponse mockContactConfirmationResponse = mock(ContactConfirmationResponse.class);
        ContactConfirmationPayload mockContactConfirmationPayload = mock(ContactConfirmationPayload.class);
        Mockito.when(userEntityRegisterMock.login(Mockito.any())).thenReturn(mockContactConfirmationResponse);
        assertNotNull(userEntityRegister.login(mockContactConfirmationPayload));
    }

    @Test
    void getPrincipal() {
        ContactConfirmationPayload mockContactConfirmationPayload = mock(ContactConfirmationPayload.class);
        Principal mockPrincipal = mock(Principal.class);
        Mockito.when(userEntityRegisterMock.getPrincipal(Mockito.any())).thenReturn(mockPrincipal);
        assertNotNull(userEntityRegister.getPrincipal(mockContactConfirmationPayload));
    }

    @Test
    void jwtLoginByPhoneNumber() {
        ContactConfirmationResponse mockContactConfirmationResponse = mock(ContactConfirmationResponse.class);
        HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        ContactConfirmationPayload mockContactConfirmationPayload = mock(ContactConfirmationPayload.class);
        Mockito.when(userEntityRegisterMock.jwtLoginByPhoneNumber(any(), any())).thenReturn(mockContactConfirmationResponse);

        assertNotNull(userEntityRegister.jwtLoginByPhoneNumber(mockHttpServletRequest, mockContactConfirmationPayload));
    }
}