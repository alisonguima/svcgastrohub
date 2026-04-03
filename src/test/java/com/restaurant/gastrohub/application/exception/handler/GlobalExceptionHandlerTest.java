package com.restaurant.gastrohub.application.exception.handler;

import com.restaurant.gastrohub.application.exception.DefaultException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("handleValidation should return 400 with field errors in ProblemDetail")
    void handleValidation_shouldReturn400WithFieldErrorsInProblemDetail() {
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(new FieldError("testObject", "email", "Email is invalid"));
        bindingResult.addError(new FieldError("testObject", "login", "Login is required"));
        bindingResult.addError(new FieldError("testObject", "email", "Email format error"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/gastrohub/api/v1/users");
        ServletWebRequest webRequest = new ServletWebRequest(request);

        ResponseEntity<ProblemDetail> response = handler.handleValidation(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getTitle()).isEqualTo("Validation Error");
        assertThat(body.getDetail()).contains("validation errors");
        assertThat(body.getProperties()).containsKey("errors");

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) body.getProperties().get("errors");
        assertThat(errors).hasSize(2);
        assertThat(errors).containsEntry("email", "Email is invalid");
        assertThat(errors).containsEntry("login", "Login is required");
        assertThat(body.getProperties()).containsKey("timestamp");
    }

    @Test
    @DisplayName("handleConflict should return 422 with duplicate email error")
    void handleConflict_shouldReturn422WithDuplicateEmailError() {

        String errorMessage = "Email already in use: test@example.com";
        DefaultException ex = new DefaultException(errorMessage);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/gastrohub/api/v1/users");
        ServletWebRequest webRequest = new ServletWebRequest(request);


        ResponseEntity<ProblemDetail> response = handler.handleConflict(ex, webRequest);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(body.getTitle()).isEqualTo("Duplicate Resource");
        assertThat(body.getDetail()).isEqualTo(errorMessage);
        assertThat(body.getProperties()).containsKey("timestamp");
    }

    @Test
    @DisplayName("handleConflict should return 404 with not found error")
    void handleConflict_shouldReturn404WithNotFoundError() {
        String errorMessage = "User not found with id: 1";
        DefaultException ex = new DefaultException(errorMessage);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/gastrohub/api/v1/users/1");
        ServletWebRequest webRequest = new ServletWebRequest(request);

        ResponseEntity<ProblemDetail> response = handler.handleConflict(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(body.getTitle()).isEqualTo("Resource Not Found");
        assertThat(body.getDetail()).isEqualTo(errorMessage);
        assertThat(body.getProperties()).containsKey("timestamp");
    }

    @Test
    @DisplayName("handleGeneric should return 500 with generic message in ProblemDetail")
    void handleGeneric_shouldReturn500WithGenericMessageInProblemDetail() {
        Exception ex = new RuntimeException("Some unexpected error");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/gastrohub/api/v1/users");
        ServletWebRequest webRequest = new ServletWebRequest(request);

        ResponseEntity<ProblemDetail> response = handler.handleGeneric(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(body.getTitle()).isEqualTo("Internal Server Error");
        assertThat(body.getDetail()).contains("unexpected error occurred");
        assertThat(body.getProperties()).containsKey("timestamp");
    }
}

