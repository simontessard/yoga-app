package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;

    @BeforeEach
    public void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password");

        Authentication mockAuthentication = Mockito.mock(Authentication.class);
        UserDetailsImpl mockUserDetails = Mockito.mock(UserDetailsImpl.class);
        User mockUser = new User("yoga@studio.com", "Test", "User", "password", false);

        Mockito.when(mockUserDetails.getUsername()).thenReturn("yoga@studio.com");
        Mockito.when(mockUserDetails.getId()).thenReturn(1L);
        Mockito.when(mockUserDetails.getFirstName()).thenReturn("Test");
        Mockito.when(mockUserDetails.getLastName()).thenReturn("User");
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        Mockito.when(jwtUtils.generateJwtToken(mockAuthentication)).thenReturn("mockJwtToken");
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()) // Code 200 OK
                .andExpect(jsonPath("$.token", not(is(emptyString())))); // Token is not empty
    }

    @Test
    public void testRegisterUser() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(result -> {
                    assertThat(result.getResponse().getStatus()).as("Status should be OK").isEqualTo(200);
                });
    }
}