package com.restaurant.gastrohub.application.exception.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    int status,
    String message,
    Map<String, String> errors,
    String timestamp) {

  public static ErrorResponse of(int status, String message, String timestamp) {
    return new ErrorResponse(status, message, null, timestamp);
  }

  public static ErrorResponse of(int status, String message, Map<String, String> errors, String timestamp) {
    return new ErrorResponse(status, message, errors, timestamp);
  }
}

