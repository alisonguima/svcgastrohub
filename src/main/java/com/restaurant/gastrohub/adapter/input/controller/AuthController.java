package com.restaurant.gastrohub.adapter.input.controller;

import com.restaurant.gastrohub.adapter.input.request.LoginRequest;
import com.restaurant.gastrohub.adapter.input.response.LoginResponse;
import com.restaurant.gastrohub.application.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v1/auth"})
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "APIs for user authentication and JWT token management")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates a user with login credentials and returns a JWT token for subsequent requests")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login request with username and password", content = @Content(schema = @Schema(implementation = LoginRequest.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully and JWT token generated", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - missing or invalid required fields"),
            @ApiResponse(responseCode = "422", description = "Invalid login or password")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("login - Receiving authentication request for user: login={}", loginRequest.login());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.authenticate(loginRequest));
    }
}

