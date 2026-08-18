package com.anuj.algotracker.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.anuj.algotracker.dto.RegisterRequest;
import com.anuj.algotracker.model.User;
import com.anuj.algotracker.repository.UserRepository;
import com.anuj.algotracker.security.JWTService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class AuthServiceTest {

    @Test
    void registerUserSuccessfully() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JWTService jwtService = mock(JWTService.class);

        AuthService authService = new AuthService(
                userRepository,
                passwordEncoder,
                authenticationManager,
                jwtService);

        RegisterRequest request = new RegisterRequest();
        request.setName("Anuj");
        request.setEmail("anuj@gmail.com");
        request.setPassword("123456");

        when(userRepository.existsByEmail("anuj@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        // Act
        authService.register(request);

        // Assert
        verify(userRepository).save(any(User.class));

        verify(passwordEncoder).encode("123456");
    }

    @Test
    void registerUserWithExistingEmail() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JWTService jwtService = mock(JWTService.class);

        AuthService authService = new AuthService(
                userRepository,
                passwordEncoder,
                authenticationManager,
                jwtService);

        RegisterRequest request = new RegisterRequest();
        request.setName("Anuj");
        request.setEmail("anuj@gmail.com");
        request.setPassword("123456");

        when(userRepository.existsByEmail("anuj@gmail.com"))
                .thenReturn(true);

        // Act + Assert
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> authService.register(request));

        // Check exception message
        Assertions.assertEquals(
                "Email already registered",
                exception.getMessage());

        // User should NOT be saved
        verify(userRepository, never()).save(any(User.class));
    }
}