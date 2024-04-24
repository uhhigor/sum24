package org.example.api.users.service;

import java.util.Optional;
import org.example.api.users.data.AuthenticationResponse;
import org.example.api.users.data.User;
import org.example.api.users.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {
        User user = new User();
        user.setUsername(request.getUsername());
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(409);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(User request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                    request.getPassword()));

            Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
            if (!optionalUser.isPresent()) {
                return new AuthenticationResponse(404);
            }

            User user = optionalUser.get();
            
            String token = jwtService.generateToken(user);
            return new AuthenticationResponse(token);
        } catch (AuthenticationException e) {
            return new AuthenticationResponse(404);
        }
        
    }

    public User getUserId(String userName) {
        return userRepository.findByUsername(userName).orElse(null);
    }
}
