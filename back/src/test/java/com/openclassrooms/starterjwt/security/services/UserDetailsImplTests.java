package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTests {
    @Test
    public void testBuilder() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password")
                .build();

        assertEquals(1L, userDetails.getId());
        assertEquals("test", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertTrue(userDetails.getAdmin());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    public void testGetAuthorities() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertTrue(authorities.isEmpty());
    }

    @Test
    public void testIsAccountNonExpired() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testEquals() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().id(1L).build();
        assertEquals(userDetails1, userDetails2);
    }
}
