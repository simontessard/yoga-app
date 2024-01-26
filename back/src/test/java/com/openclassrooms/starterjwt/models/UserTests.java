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
    public void testFourArgConstructor() {
        User user = new User("john.doe@gmail.com", "Doe", "John", "password");
        assertEquals("john.doe@gmail.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password", user.getPassword());
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

    @Test
    public void testUserBuilderToString() {
        String userBuilder = User.builder().toString();

        String expectedUserBuilder = "User.UserBuilder(id=null, email=null, lastName=null, firstName=null, password=null, admin=false, createdAt=null, updatedAt=null)";
        assertEquals(expectedUserBuilder, userBuilder);
    }

    @Test
    public void testNoArgsConstructor() {
        User user = new User();
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmail());
    }

    @Test
    public void testRequiredArgsConstructor() {
        User user = new User();
        user.setFirstName("John");
        assertEquals("John", user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmail());
    }

    @Test
    public void testAllArgsConstructor() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    public void testBuilder() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("johntest")
                .build();
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
    }
}