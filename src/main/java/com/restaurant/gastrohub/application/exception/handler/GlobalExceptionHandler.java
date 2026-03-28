package com.restaurant.gastrohub.application.exception.handler;

import com.restaurant.gastrohub.application.exception.error.ErrorResponse;
import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.exception.DefaultException;
import com.restaurant.gastrohub.application.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  // 400 - Validation errors from @Valid
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> fieldErrors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            error -> Objects.requireNonNullElse(error.getDefaultMessage(), "Invalid value"),
            (existing, duplicate) -> existing));

    log.warn("handleValidation - Validation failed: fields={}", fieldErrors.keySet());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            fieldErrors,
            DateTimeUtils.getDateTimeZoneUTC()));
  }

  // 422 - Conflict (duplicate email, login, etc.)
  @ExceptionHandler(DefaultException.class)
  public ResponseEntity<ErrorResponse> handleConflict(DefaultException ex) {
    log.warn("handleConflict - Conflict: message={}", ex.getMessage());

    return ResponseEntity
        .status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(ErrorResponse.of(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            ex.getMessage(),
            DateTimeUtils.getDateTimeZoneUTC()));
  }

  // 500 - Unexpected errors
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    log.error("handleGeneric - Unexpected error: message={}", ex.getMessage(), ex);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            DateTimeUtils.getDateTimeZoneUTC()));
  }
}

