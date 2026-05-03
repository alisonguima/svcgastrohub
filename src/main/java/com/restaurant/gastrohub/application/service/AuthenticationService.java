package com.restaurant.gastrohub.application.service;

import com.restaurant.gastrohub.adapter.input.request.LoginRequest;
import com.restaurant.gastrohub.adapter.input.response.LoginResponse;
import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.exception.DefaultException;
import com.restaurant.gastrohub.application.port.output.UserPostgresPort;
import com.restaurant.gastrohub.application.util.JwtTokenProvider;
import com.restaurant.gastrohub.application.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserPostgresPort userPostgresPort;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public LoginResponse authenticate(LoginRequest loginRequest) {
    log.info("authenticate - Attempting to authenticate user: login={}", loginRequest.login());

    User user = userPostgresPort.getUserByLogin(loginRequest.login());

    Optional.of(passwordEncoder.matches(loginRequest.password(), user.getPassword()))
        .filter(matches -> matches)
        .orElseThrow(() -> {
          log.warn("authenticate - Invalid password for user: login={}", loginRequest.login());
          return new DefaultException(ApiConstants.INVALID_CREDENTIALS);
        });

    String token = jwtTokenProvider.generateToken(user.getId(), user.getLogin(), user.getUserType().name());

    log.info("authenticate - User authenticated successfully: login={}, userId={}", loginRequest.login(), user.getId());

    return new LoginResponse(token, user.getId(), user.getLogin());
  }
}

