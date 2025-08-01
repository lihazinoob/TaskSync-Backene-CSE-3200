package com.example.careerPilot.demo.service;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.exception.ResourceNotFoundException;
import com.example.careerPilot.demo.model.AuthenticationResponse;
import com.example.careerPilot.demo.model.LoginRequest;
import com.example.careerPilot.demo.model.RegisterRequest;
import com.example.careerPilot.demo.repository.userRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final userRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(userRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    public AuthenticationResponse register(RegisterRequest request ){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.ROLE.USER);
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user = repository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }
    public AuthenticationResponse authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

        } catch (Exception e) {
            throw new ResourceNotFoundException(request.getUsername());
        }

        User user = repository.findByUsername(request.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found with user name" + request.getUsername()));
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);

    }

}
