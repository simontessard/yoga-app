package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTests {
    private Teacher teacher;
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();

    @BeforeEach
    public void setUp() {

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");
        teacher.setCreatedAt(createdAt);
        teacher.setUpdatedAt(updatedAt);
    }

    @Test
    public void testTeacherGetterAndSetters() {

        assertEquals(1L, teacher.getId());
        assertEquals("Doe", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
        assertEquals(createdAt, teacher.getCreatedAt());
        assertEquals(updatedAt, teacher.getUpdatedAt());
    }

    @Test
    public void testTeacherEqualsAndHashCode() {
        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);
        teacher2.setLastName("Doe");
        teacher2.setFirstName("John");

        assertEquals(teacher, teacher2);
        assertEquals(teacher.hashCode(), teacher2.hashCode());
    }

    @Test
    public void testTeacherToString() {
        String expected = "Teacher(id=1, lastName=Doe, firstName=John, createdAt=" + teacher.getCreatedAt()
                + ", updatedAt=" + teacher.getUpdatedAt() + ")";
        assertEquals(expected, teacher.toString());
    }
}
