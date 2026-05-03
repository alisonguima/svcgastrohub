package com.restaurant.gastrohub.application.service;

import com.restaurant.gastrohub.adapter.input.request.LoginRequest;
import com.restaurant.gastrohub.adapter.input.response.LoginResponse;
import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import com.restaurant.gastrohub.application.domain.user.User;
import com.restaurant.gastrohub.application.exception.DefaultException;
import com.restaurant.gastrohub.application.port.output.UserPostgresPort;
import com.restaurant.gastrohub.application.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserPostgresPort userPostgresPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthenticationService authenticationService;

    private LoginRequest loginRequest;
    private User user;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("johndoe", "Password123!");
        encodedPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36lrRjlm";
        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .login("johndoe")
                .password(encodedPassword)
                .userType(UserType.CUSTOMER)
                .address("123 Main St")
                .build();
    }

    @Test
    void authenticate_WithValidCredentials_ReturnsLoginResponse() {
        when(userPostgresPort.getUserByLogin("johndoe")).thenReturn(user);
        when(passwordEncoder.matches("Password123!", encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.generateToken(1L, "johndoe", UserType.CUSTOMER.name())).thenReturn("jwt-token-123");

        LoginResponse response = authenticationService.authenticate(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token-123", response.token());
        assertEquals(1L, response.userId());
        assertEquals("johndoe", response.login());
        assertEquals("Bearer", response.type());
    }

    @Test
    void authenticate_WithInvalidPassword_ThrowsDefaultException() {
        when(userPostgresPort.getUserByLogin("johndoe")).thenReturn(user);
        when(passwordEncoder.matches("WrongPassword", encodedPassword)).thenReturn(false);

        assertThrows(DefaultException.class, () -> authenticationService.authenticate(
                new LoginRequest("johndoe", "WrongPassword")));
    }

    @Test
    void authenticate_WithNonExistentUser_ThrowsDefaultException() {
        when(userPostgresPort.getUserByLogin("nonexistent")).thenThrow(
                new DefaultException(ApiConstants.INVALID_CREDENTIALS));

        assertThrows(DefaultException.class, () -> authenticationService.authenticate(
                new LoginRequest("nonexistent", "Password123!")));
    }
}

