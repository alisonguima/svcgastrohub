package com.restaurant.gastrohub.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    @Test
    @DisplayName("passwordEncoder should return BCryptPasswordEncoder instance")
    void passwordEncoder_shouldReturnBCryptPasswordEncoderInstance() {

        SecurityConfig config = new SecurityConfig();


        PasswordEncoder encoder = config.passwordEncoder();


        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    @DisplayName("passwordEncoder should encode and match passwords")
    void passwordEncoder_shouldEncodeAndMatchPasswords() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        String rawPassword = "testPassword";

        String encodedPassword = passwordEncoder.encode(rawPassword);
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("passwordEncoder should not match wrong password")
    void passwordEncoder_shouldNotMatchWrongPassword() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        String rawPassword = "testPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        boolean matches = passwordEncoder.matches(wrongPassword, encodedPassword);

        assertThat(matches).isFalse();
    }
}
