package org.example.api.component;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserComponentTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Success() {
        User userRequest = new User();
        userRequest.setUsername("testuser");
        userRequest.setPassword("password");
        userRequest.setRole(Role.valueOf("USER"));

        User user = new User();
        user.setId(1);
        user.setUsername(userRequest.getUsername());
        user.setPassword("encodedPassword");
        user.setRole(userRequest.getRole());

        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("token");

        AuthenticationResponse response = authenticationService.register(userRequest);
    }

    @Test
    public void testLogin_Success() {
        User userRequest = new User();
        userRequest.setUsername("testuser");
        userRequest.setPassword("password");

        User user = new User();
        user.setId(1);
        user.setUsername(userRequest.getUsername());
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtService.generateToken(user)).thenReturn("token");

        AuthenticationResponse response = authenticationService.login(userRequest);

        assertNotNull(response.getToken());
        assertEquals("token", response.getToken());
    }
    @Test
    public void testGetUserId_Success() {
        String username = "testuser";

        User user = new User();
        user.setId(1);
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = authenticationService.getUserId(username);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void testGetUserId_UserNotFound() {
        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        User result = authenticationService.getUserId(username);

        assertNull(result);
    }

    @Test
    public void testDeleteUser_UserExists() {
        String username = "testuser";

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        authenticationService.deleteUser(username);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        authenticationService.deleteUser(username);

        verify(userRepository, never()).delete(any());
    }
}
