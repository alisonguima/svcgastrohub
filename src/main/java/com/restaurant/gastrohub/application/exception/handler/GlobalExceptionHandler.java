package com.restaurant.gastrohub.application.exception.handler;

import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.exception.DefaultException;
import com.restaurant.gastrohub.application.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final Map<String, ErrorDetails> ERROR_DETAILS_MAP = new LinkedHashMap<>();

  static {
    ERROR_DETAILS_MAP.put("already in use", new ErrorDetails(ApiConstants.ERROR_TYPE_DUPLICATE_RESOURCE, ApiConstants.ERROR_TITLE_DUPLICATE_RESOURCE));
    ERROR_DETAILS_MAP.put("not found", new ErrorDetails(ApiConstants.ERROR_TYPE_RESOURCE_NOT_FOUND, ApiConstants.ERROR_TITLE_RESOURCE_NOT_FOUND));
    ERROR_DETAILS_MAP.put("Invalid", new ErrorDetails(ApiConstants.ERROR_TYPE_INVALID_REQUEST, ApiConstants.ERROR_TITLE_INVALID_REQUEST));
  }


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidation(
      MethodArgumentNotValidException ex, WebRequest request) {

    Map<String, String> fieldErrors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            error -> Objects.requireNonNullElse(error.getDefaultMessage(), "Invalid value"),
            (existing, duplicate) -> existing));

    log.warn("handleValidation - Validation failed: fields={}", fieldErrors.keySet());

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setType(URI.create(ApiConstants.PROBLEM_DETAIL_TYPE_BASE + ApiConstants.ERROR_TYPE_VALIDATION));
    problemDetail.setTitle(ApiConstants.ERROR_TITLE_VALIDATION);
    problemDetail.setDetail(ApiConstants.ERROR_DETAIL_VALIDATION);
    problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
    problemDetail.setProperty("errors", fieldErrors);
    problemDetail.setProperty("timestamp", DateTimeUtils.getProblemDetailTimestamp());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(problemDetail);
  }

  @ExceptionHandler(DefaultException.class)
  public ResponseEntity<ProblemDetail> handleConflict(
      DefaultException ex, WebRequest request) {

    log.warn("handleConflict - Conflict: message={}", ex.getMessage());

    ErrorDetails errorDetails = findErrorDetails(ex.getMessage());
    HttpStatus status = determineStatus(ex.getMessage());

    ProblemDetail problemDetail = ProblemDetail.forStatus(status);
    problemDetail.setType(URI.create(ApiConstants.PROBLEM_DETAIL_TYPE_BASE + errorDetails.errorType()));
    problemDetail.setTitle(errorDetails.title());
    problemDetail.setDetail(ex.getMessage());
    problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
    problemDetail.setProperty("timestamp", DateTimeUtils.getProblemDetailTimestamp());

    return ResponseEntity
        .status(status)
        .body(problemDetail);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleGeneric(
      Exception ex, WebRequest request) {

    log.error("handleGeneric - Unexpected error: message={}", ex.getMessage(), ex);

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problemDetail.setType(URI.create(ApiConstants.PROBLEM_DETAIL_TYPE_BASE + ApiConstants.ERROR_TYPE_INTERNAL_SERVER));
    problemDetail.setTitle(ApiConstants.ERROR_TITLE_INTERNAL_SERVER);
    problemDetail.setDetail(ApiConstants.ERROR_DETAIL_INTERNAL_SERVER);
    problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
    problemDetail.setProperty("timestamp", DateTimeUtils.getProblemDetailTimestamp());

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(problemDetail);
  }

  private ErrorDetails findErrorDetails(String message) {
    return ERROR_DETAILS_MAP.entrySet().stream()
        .filter(entry -> message.contains(entry.getKey()))
        .map(Map.Entry::getValue)
        .findFirst()
        .orElse(new ErrorDetails(ApiConstants.ERROR_TYPE_CONFLICT, ApiConstants.ERROR_TITLE_UNPROCESSABLE_ENTITY));
  }

  private HttpStatus determineStatus(String message) {
    return message.contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.UNPROCESSABLE_ENTITY;
  }

  private record ErrorDetails(String errorType, String title) {}
}


