package org.example.api.units;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.api.users.data.User;
import org.example.api.users.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
    }

    @Test
    public void testGenerateToken() {
        String token = jwtService.generateToken(user);

        assertNotNull(token);
        String username = jwtService.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    public void testValidateToken_Valid() {
        String token = jwtService.generateToken(user);

        when(userDetails.getUsername()).thenReturn("testUser");
        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    public void testValidateToken_InvalidUsername() {
        String token = jwtService.generateToken(user);

        when(userDetails.getUsername()).thenReturn("anotherUser");
        boolean isValid = jwtService.validateToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    public void testExtractUsername() {
        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    public void testExtractClaim() {
        try {
            String token = jwtService.generateToken(user);

            Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

        } catch (ExpiredJwtException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testIsTokenExpired_NotExpired() {
        String token = jwtService.generateToken(user);

        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    public void testIsTokenExpired_Expired() {
        try {
            String token = Jwts.builder()
                    .subject("testUser")
                    .issuedAt(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)) // Issued 1 day ago
                    .expiration(new Date(System.currentTimeMillis() - 12 * 60 * 60 * 1000)) // Expired 12 hours ago
                    .signWith(jwtService.getSecretKey())
                    .compact();
        } catch (ExpiredJwtException e){
            assertTrue(jwtService.isTokenExpired(String.valueOf(e.getClaims().getExpiration())));
        }

    }
}
