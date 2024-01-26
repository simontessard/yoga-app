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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;

import java.util.Optional;

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

    private SignupRequest signupRequest;

    @BeforeEach
    public void setUpForValidTests() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password");

        Authentication mockAuthentication = Mockito.mock(Authentication.class);
        // Mock object for UserDetails. This is used to simulate the real UserDetails
        // object in tests.
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
    public void testValidAuthenticateUser() throws Exception {
        LoginRequest validloginRequest = new LoginRequest();
        validloginRequest.setEmail("yoga@studio.com");
        validloginRequest.setPassword("test!1234");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validloginRequest)))
                .andExpect(status().isOk()) // Code 200 OK
                .andExpect(jsonPath("$.token", not(is(emptyString())))); // Token is not empty
    }

    @Test
    public void testInvalidAuthenticateUser() throws Exception {
        LoginRequest invalidLoginRequest = new LoginRequest();
        invalidLoginRequest.setEmail("invalidEmail@test.fr"); // This email is not valid
        invalidLoginRequest.setPassword("1234566");

        // Configuration mocks for failed authentication
        Mockito.when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLoginRequest)))
                .andExpect(status().isUnauthorized()) // Code 401 Unauthorized
                .andExpect(jsonPath("$.error", is("Unauthorized"))) // Error message is "Unauthorized"
                .andExpect(jsonPath("$.message", is("Bad credentials"))); // Message is "Bad credentials"
    }

    @Test
    public void testRegisterUser() throws Exception {
        // Checking if the request body is correctly mapped to the SignupRequest object
        SignupRequest expectedSignupRequest = new SignupRequest();
        expectedSignupRequest.setEmail("test@test.com");
        expectedSignupRequest.setFirstName("Test");
        expectedSignupRequest.setLastName("User");
        expectedSignupRequest.setPassword("password");
        assertEquals(expectedSignupRequest, signupRequest);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk()) // Code 200 OK
                .andExpect(jsonPath("$.message", is("User registered successfully!"))); // Token is not empty
    }
}