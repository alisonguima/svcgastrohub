package com.restaurant.gastrohub.application.util;

import com.restaurant.gastrohub.application.domain.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret",
                "mySecretKeyForJWTTokenGenerationAndValidationPleaseChangeInProduction");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", 86400000L);
    }

    @Test
    void generateToken_WithValidData_ReturnsValidToken() {
        String token = jwtTokenProvider.generateToken(1L, "johndoe", UserType.OWNER.name());

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void validateToken_WithValidToken_ReturnsTrue() {
        String token = jwtTokenProvider.generateToken(1L, "johndoe", UserType.OWNER.name());

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateToken_WithInvalidToken_ReturnsFalse() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void extractLogin_WithValidToken_ReturnsLogin() {
        String token = jwtTokenProvider.generateToken(1L, "johndoe", UserType.OWNER.name());

        String login = jwtTokenProvider.extractLogin(token);

        assertEquals("johndoe", login);
    }

    @Test
    void extractUserId_WithValidToken_ReturnsUserId() {
        String token = jwtTokenProvider.generateToken(1L, "johndoe", UserType.OWNER.name());

        Long userId = jwtTokenProvider.extractUserId(token);

        assertEquals(1L, userId);
    }

    @Test
    void generateToken_WithDifferentUsers_GeneratesDifferentTokens() {
        String token1 = jwtTokenProvider.generateToken(1L, "johndoe", UserType.OWNER.name());
        String token2 = jwtTokenProvider.generateToken(2L, "janedoe", UserType.OWNER.name());

        assertNotNull(token1);
        assertNotNull(token2);
        assertTrue(!token1.equals(token2));
    }
}

