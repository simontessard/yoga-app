package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.lang.reflect.Field;
import java.util.Date;

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

    @Test
    public void testValidateJwtToken_InvalidSignature() {
        String invalidSignatureToken = "tokenWithInvalidSignature";
        assertFalse(jwtUtils.validateJwtToken(invalidSignatureToken));
    }

    @Test
    public void testValidateJwtToken_MalformedToken() {
        String malformedToken = "125436587894498de";
        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }

    @Test
    public void testValidateJwtToken_ExpiredToken() throws InterruptedException {
        // Générer un token qui expire après 1 seconde
        String jwtSecret = "yourSecretKey";
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000);

        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Attendre 2 secondes pour que le token expire
        Thread.sleep(2000);

        // Vérifier que le token est expiré
        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    public void testValidateJwtToken_UnsupportedToken() {
        String unsupportedToken = "087258a5-ddb2-487e-a38e-071698896ff9";
        assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
    }
}
