package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.TeacherService;

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
public class TeacherControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private TeacherMapper teacherMapper;

    @MockBean
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setup() {
        Mockito.when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("token");
        Mockito.when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);

        // Creating a mock teacher
        Teacher mockTeacher = new Teacher();
        // Set other properties of the mock teacher as needed
        mockTeacher.setId(1L);
        // Mock teacherRepository to return mock teacher for findById(1L)
        Mockito.when(teacherService.findById(1L)).thenReturn(mockTeacher);
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234")
    public void testFindById() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByIdIsProtected() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234")
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.isA(JSONArray.class)));
    }

    @Test
    public void testFindAllisProtected() throws Exception {
        mockMvc.perform(get("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234")
    public void testFindByIdNotFound() throws Exception {
        // Mock teacherService to return null for findById(2L)
        Mockito.when(teacherService.findById(2L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234")
    public void testFindByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", "invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}