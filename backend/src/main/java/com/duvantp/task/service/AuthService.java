package com.duvantp.task.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.duvantp.task.Auth.AuthResponse;
import com.duvantp.task.Auth.LoginRequest;
import com.duvantp.task.Auth.RegisterRequest;
import com.duvantp.task.models.Role;
import com.duvantp.task.models.User;
import com.duvantp.task.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder; 
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);

        return AuthResponse.builder().token(token).build();

    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword())) 
        .firtname(request.getFirtname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .role(Role.USER)
        .build();

        userRepository.save(user);

        return AuthResponse.builder().token(jwtService.getToken(user)).build();
        
    }

}
