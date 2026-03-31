package com.restaurant.gastrohub.application.exception.handler;

import com.restaurant.gastrohub.application.exception.DefaultException;
import com.restaurant.gastrohub.application.exception.error.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("handleValidation should return 400 with field errors")
    void handleValidation_shouldReturn400WithFieldErrors() {
        // Arrange
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(new FieldError("testObject", "email", "Email is invalid"));
        bindingResult.addError(new FieldError("testObject", "login", "Login is required"));
        bindingResult.addError(new FieldError("testObject", "email", "Email format error")); // Duplicate field to cover merge
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.message()).isEqualTo("Validation failed");
        assertThat(body.errors()).isNotNull();
        assertThat(body.errors()).hasSize(2); // email and login
        assertThat(body.errors()).containsEntry("email", "Email is invalid"); // First one wins due to merge
        assertThat(body.errors()).containsEntry("login", "Login is required");
        assertThat(body.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("handleConflict should return 422 with exception message")
    void handleConflict_shouldReturn422WithExceptionMessage() {
        // Arrange
        String errorMessage = "User not found with id: 1";
        DefaultException ex = new DefaultException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleConflict(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.status()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(body.message()).isEqualTo(errorMessage);
        assertThat(body.errors()).isNull();
        assertThat(body.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("handleGeneric should return 500 with generic message")
    void handleGeneric_shouldReturn500WithGenericMessage() {
        // Arrange
        Exception ex = new RuntimeException("Some unexpected error");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleGeneric(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(body.message()).isEqualTo("An unexpected error occurred");
        assertThat(body.errors()).isNull();
        assertThat(body.timestamp()).isNotNull();
    }
}
