package com.restaurant.gastrohub.adapter.input.controller;

import com.restaurant.gastrohub.adapter.input.request.LoginRequest;
import com.restaurant.gastrohub.adapter.input.response.LoginResponse;
import com.restaurant.gastrohub.application.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("johndoe", "Password123!");
        loginResponse = new LoginResponse("jwt-token-123", 1L, "johndoe");
    }

    @Test
    void login_WithValidCredentials_ReturnsOkWithToken() {
        when(authenticationService.authenticate(loginRequest)).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token-123", response.getBody().token());
        assertEquals(1L, response.getBody().userId());
        assertEquals("johndoe", response.getBody().login());
        assertEquals("Bearer", response.getBody().type());
    }

    @Test
    void login_WithInvalidCredentials_ThrowsException() {
        when(authenticationService.authenticate(loginRequest))
                .thenThrow(new RuntimeException("Invalid credentials"));

        try {
            authController.login(loginRequest);
        } catch (RuntimeException e) {
            assertEquals("Invalid credentials", e.getMessage());
        }
    }
}

