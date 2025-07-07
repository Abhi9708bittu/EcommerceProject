package com.example.ecommerce.controller;

import org.example.ecommerce.controller.AuthController;
import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    private UserRepository userRepository;
    private AuthController authController;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        authController = new AuthController(userRepository);
    }

    @Test
    void test_register_success() {
        User user = new User(null, "testuser", "testpass");
        when(userRepository.findByUsername("testuser")).thenReturn(null);
        String result = authController.register(user);
        verify(userRepository).save(user);
        assertEquals("Registration successful", result);
    }

    @Test
    void test_register_duplicate_username() {
        User user = new User(null, "testuser", "testpass");
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        String result = authController.register(user);
        assertEquals("Username already exists", result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void test_login_success() {
        User user = new User(null, "testuser", "testpass");
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        String result = authController.login(new User(null, "testuser", "testpass"));
        assertEquals("Login successful", result);
    }

    @Test
    void test_login_wrong_password() {
        User user = new User(null, "testuser", "testpass");
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        String result = authController.login(new User(null, "testuser", "wrongpass"));
        assertEquals("Invalid credentials", result);
    }

    @Test
    void test_login_user_not_found() {
        when(userRepository.findByUsername("nouser")).thenReturn(null);
        String result = authController.login(new User(null, "nouser", "pass"));
        assertEquals("Invalid credentials", result);
    }
} 