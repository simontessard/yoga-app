package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

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

    @Test
    public void testCreateSession() {
        String name = "Zumba Session";
        Session session = Session.builder().name(name).build();

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName(name);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> responseEntity = sessionController.create(sessionDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sessionDto, responseEntity.getBody());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234")
    public void testDeleteSession() throws Exception {
        // Create a new session object
        Session session = new Session();
        session.setId(1L);
        session.setName("Zumba");
        session.setCreatedAt(LocalDateTime.parse("2024-01-13T00:00:00"));
        session.setDescription("Carnaval");

        // Mock sessionService to return the session when getById is called
        Mockito.when(sessionService.getById(any(Long.class))).thenReturn(session);

        // Mock sessionService to do nothing when delete is called
        Mockito.doNothing().when(sessionService).delete(any(Long.class));

        // Perform the DELETE request
        mockMvc.perform(delete("/api/session/" + session.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that sessionService.getById and sessionService.delete were called
        Mockito.verify(sessionService, times(1)).getById(session.getId());
        Mockito.verify(sessionService, times(1)).delete(session.getId());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234")
    public void testUpdateSession() throws Exception {
        // Create a new teacher object
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        // Create a new session object
        Session session = new Session();
        session.setId(1L);
        session.setName("Zumba");
        session.setCreatedAt(LocalDateTime.parse("2024-01-13T00:00:00"));
        session.setDescription("Carnaval");
        session.setTeacher(teacher);

        // Create a session DTO object
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Zumba Updated");
        sessionDto.setDescription("Carnaval Updated");

        // Mock sessionService to return the updated session when update is called
        Mockito.when(sessionService.update(any(Long.class), any(Session.class))).thenReturn(session);

        // Mock sessionMapper to return the sessionDto when toDto is called
        Mockito.when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        // Perform the PUT request
        mockMvc.perform(put("/api/session/" + session.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"name\":\"Zumba Updated\",\"date\":\"2024-01-13T00:00:00\",\"description\":\"Carnaval Updated\",\"teacher_id\":1}")) // Add
                                                                                                                                                // \"teacher_id\":1
                .andExpect(status().isOk());
    }
}