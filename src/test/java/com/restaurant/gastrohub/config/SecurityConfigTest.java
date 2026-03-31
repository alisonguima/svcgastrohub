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
        // Arrange
        SecurityConfig config = new SecurityConfig();

        // Act
        PasswordEncoder encoder = config.passwordEncoder();

        // Assert
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    @DisplayName("passwordEncoder should encode and match passwords")
    void passwordEncoder_shouldEncodeAndMatchPasswords() {
        // Arrange
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        String rawPassword = "testPassword";

        // Act
        String encodedPassword = passwordEncoder.encode(rawPassword);
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        // Assert
        assertThat(encodedPassword).isNotEqualTo(rawPassword); // Encoded should be different
        assertThat(matches).isTrue(); // Should match the raw password
    }

    @Test
    @DisplayName("passwordEncoder should not match wrong password")
    void passwordEncoder_shouldNotMatchWrongPassword() {
        // Arrange
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        String rawPassword = "testPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Act
        boolean matches = passwordEncoder.matches(wrongPassword, encodedPassword);

        // Assert
        assertThat(matches).isFalse(); // Should not match wrong password
    }
}
