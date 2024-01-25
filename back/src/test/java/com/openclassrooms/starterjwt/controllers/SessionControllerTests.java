package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.SessionService;

import net.minidev.json.JSONArray;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.core.Authentication;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private SessionMapper sessionMapper;

    @MockBean
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setup() {
        Mockito.when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("token");
        Mockito.when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);

        // Creating a mock session
        Session mockSession = new Session();
        // Set other properties of the mock session as needed
        mockSession.setId(1L);
        // Mock sessionRepository to return mock session for findById(1L)
        Mockito.when(sessionService.getById(1L)).thenReturn(mockSession);
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234")
    public void testFindById() throws Exception {
        mockMvc.perform(get("/api/session/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByIdIsProtected() throws Exception {
        mockMvc.perform(get("/api/session/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234")
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.isA(JSONArray.class)));
    }

    @Test
    public void testFindAllisProtected() throws Exception {
        mockMvc.perform(get("/api/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}