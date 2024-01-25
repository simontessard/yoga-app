package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("simon.durand@gmail.com", "Durand", "Simon", "password", true);
    }

    @Test
    public void testUserGetterAndSetters() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        user.setId(1L);
        user.setAdmin(true);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);

        assertEquals(1L, user.getId());
        assertEquals("simon.durand@gmail.com", user.getEmail());
        assertEquals("Durand", user.getLastName());
        assertEquals("Simon", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
    }

    @Test
    public void testUserEqualsAndHashCode() {
        User user2 = new User("simon.durand@gmail.com", "Durand", "Simon", "password", true);
        assertEquals(user, user2);
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    public void testUserToString() {
        String expected = "User(id=null, email=simon.durand@gmail.com, lastName=Durand, firstName=Simon, password=password, admin=true, createdAt=null, updatedAt=null)";
        assertEquals(expected, user.toString());
    }
}