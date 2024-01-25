package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtUtilsTests {
    private JwtUtils jwtUtils;
    private String jwtSecret = "testSecret";
    private int jwtExpirationMs = 600000;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        jwtUtils = new JwtUtils();

        Field jwtSecretField = JwtUtils.class.getDeclaredField("jwtSecret");
        jwtSecretField.setAccessible(true);
        jwtSecretField.set(jwtUtils, jwtSecret);

        Field jwtExpirationMsField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        jwtExpirationMsField.setAccessible(true);
        jwtExpirationMsField.set(jwtUtils, jwtExpirationMs);

        authentication = mock(Authentication.class);
    }

    @Test
    public void testGenerateJwtToken() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    public void testValidateJwtToken() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }
}
