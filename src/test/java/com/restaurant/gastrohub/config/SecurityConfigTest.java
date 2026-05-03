package com.restaurant.gastrohub.config;

import com.restaurant.gastrohub.application.util.JwtTokenProvider;
import com.restaurant.gastrohub.config.security.JwtAuthenticationEntryPoint;
import com.restaurant.gastrohub.config.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SecurityConfig Tests")
@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @DisplayName("passwordEncoder should return BCryptPasswordEncoder instance")
    void passwordEncoder_shouldReturnBCryptPasswordEncoderInstance() {
        SecurityConfig config = new SecurityConfig(jwtTokenProvider, jwtAuthenticationEntryPoint);

        PasswordEncoder encoder = config.passwordEncoder();

        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    @DisplayName("passwordEncoder should encode and match passwords")
    void passwordEncoder_shouldEncodeAndMatchPasswords() {
        SecurityConfig config = new SecurityConfig(jwtTokenProvider, jwtAuthenticationEntryPoint);
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        String rawPassword = "testPassword123";

        String encodedPassword = passwordEncoder.encode(rawPassword);
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("passwordEncoder should not match wrong password")
    void passwordEncoder_shouldNotMatchWrongPassword() {
        SecurityConfig config = new SecurityConfig(jwtTokenProvider, jwtAuthenticationEntryPoint);
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        String rawPassword = "testPassword123";
        String wrongPassword = "wrongPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        boolean matches = passwordEncoder.matches(wrongPassword, encodedPassword);

        assertThat(matches).isFalse();
    }

    @Test
    @DisplayName("jwtAuthenticationFilter should return JwtAuthenticationFilter instance")
    void jwtAuthenticationFilter_shouldReturnJwtAuthenticationFilterInstance() {
        SecurityConfig config = new SecurityConfig(jwtTokenProvider, jwtAuthenticationEntryPoint);

        JwtAuthenticationFilter filter = config.jwtAuthenticationFilter();

        assertThat(filter).isInstanceOf(JwtAuthenticationFilter.class);
    }
}
