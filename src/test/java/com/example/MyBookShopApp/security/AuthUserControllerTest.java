package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.user.UserEntity;
import com.example.MyBookShopApp.exception.UserNotFoundException;
import com.example.MyBookShopApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthUserControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private UserService userServiceMock;

    @MockBean
    private UserEntityRepository userEntityRepositoryMock;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public AuthUserControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @AfterAll
    static void afterAll() {
    }

    @Test
    void handleSignIn() throws Exception {
        mockMvc.perform(get("/signin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void handleSignUp() throws Exception {
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void handleRequestContactConfirmation_withValidPayload_getOk() throws Exception {
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact("test@example.com");
        payload.setCode("1234567");

        mockMvc.perform(post("/requestContactConfirmation")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void handleRequestContactConfirmation_withInvalidPayload_getError() throws Exception {
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact("error@example.com");
        payload.setCode("1234567");

        mockMvc.perform(post("/requestContactConfirmation")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }

    @Test
    void handleApproveContact_getOk() throws Exception {
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact("test@example.com");
        payload.setCode("1234567");

        MvcResult result = mockMvc.perform(post("/approveContact")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void handleUserRegistration_getAttribute() throws Exception {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.org");
        registrationForm.setName("Tester");
        registrationForm.setPassword("iddqd");
        registrationForm.setPhone("9011234567");

        UserEntityRegister mockUserEntityRegister = mock(UserEntityRegister.class);

        UserEntity mockUserEntity = mock(UserEntity.class);

        Mockito.when(userEntityRepositoryMock.findUserEntityByEmail(Mockito.any())).thenReturn(mockUserEntity);
        Mockito.when(mockUserEntityRegister.registerNewUser(Mockito.any())).thenReturn(mockUserEntity);

        mockMvc.perform(post("/reg")
                .content(objectMapper.writeValueAsString(registrationForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@example.com", password = "1234567", roles = "USER")
    void handleLogin_getOk() throws Exception {
        mockMvc.perform(get("/login")
                .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@example.com", password = "1234567", roles = "USER")
    void handleMy() throws Exception {
        mockMvc.perform(get("/my"))
                .andExpect(status().isOk());
    }

    @Test
    void handleProfile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection());
    }
}