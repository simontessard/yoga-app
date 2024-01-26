package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTests {
    private Session session;

    @BeforeEach
    public void setUp() {
        session = new Session();
    }

    @Test
    public void testSessionGetterAndSetters() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = new Teacher();
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setAdmin(false);

        session.setId(1L);
        session.setName("Test Session");
        session.setDate(date);
        session.setDescription("Test Description");
        session.setTeacher(teacher);
        session.setUsers(Collections.singletonList(user));
        session.setCreatedAt(createdAt);
        session.setUpdatedAt(updatedAt);

        assertEquals(1L, session.getId());
        assertEquals("Test Session", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("Test Description", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(Collections.singletonList(user), session.getUsers());
        assertEquals(createdAt, session.getCreatedAt());
        assertEquals(updatedAt, session.getUpdatedAt());
    }

    @Test
    public void testSessionEqualsAndHashCode() {
        Session session2 = new Session();

        assertEquals(session, session2);
        assertEquals(session.hashCode(), session2.hashCode());
    }

    @Test
    public void testSessionToString() {
        Date date = new Date();
        session.setId(1L);
        session.setName("Test Session");
        session.setDate(date);
        session.setDescription("Test Description");

        String expected = "Session(id=1, name=Test Session, date=" + date
                + ", description=Test Description, teacher=null, users=null, createdAt=null, updatedAt=null)";
        assertEquals(expected, session.toString());
    }

    @Test
    public void testSessionBuilderToString() {
        String sessionBuilder = Session.builder().toString();

        String expectedSessionBuilder = "Session.SessionBuilder(id=" + null + ", name=" + null + ", date=" + null
                + ", description=" + null + ", teacher=" + null + ", users=" + null + ", createdAt=" + null
                + ", updatedAt=" + null + ")";
        assertEquals(expectedSessionBuilder, sessionBuilder);
    }
}
