package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TeacherServiceTests {
    @Mock
    private TeacherRepository teacherRepository;

    private TeacherService teacherService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    public void testFindAll() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        verify(teacherRepository, times(1)).findAll();
        assertEquals(teachers, result);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(id);

        verify(teacherRepository, times(1)).findById(id);
        assertEquals(teacher, result);
    }
}
