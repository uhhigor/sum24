package org.example.api.units;

import org.example.api.users.data.AuthenticationResponse;
import org.example.api.users.data.Role;
import org.example.api.users.data.User;
import org.example.api.users.repositories.UserRepository;
import org.example.api.users.service.AuthenticationService;
import org.example.api.users.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_UserAlreadyExists() {
        User request = new User();
        request.setUsername("existingUser");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        AuthenticationResponse response = authenticationService.register(request);

        verify(userRepository, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    public void testRegister_Success() {
        User request = new User();
        request.setUsername("newUser");
        request.setPassword("password");
        request.setRole(Role.valueOf("USER"));

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response.getToken());
        assertEquals("jwtToken", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testLogin_Success() {
        User request = new User();
        request.setUsername("existingUser");
        request.setPassword("password");

        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.login(request);

        assertNotNull(response.getToken());
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    public void testGetUserId_UserFound() {
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User result = authenticationService.getUserId("existingUser");

        assertEquals(user, result);
    }

    @Test
    public void testGetUserId_UserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        User result = authenticationService.getUserId("nonExistingUser");

        assertNull(result);
    }

    @Test
    public void testDeleteUser_UserExists() {
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        authenticationService.deleteUser("existingUser");

        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        authenticationService.deleteUser("nonExistingUser");

        verify(userRepository, never()).delete(any(User.class));
    }
}

