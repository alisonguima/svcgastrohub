package com.restaurant.gastrohub.application.exception.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response structure")
public record ErrorResponse(

    @Schema(description = "HTTP status code", example = "400")
    int status,

    @Schema(description = "Error message", example = "Validation failed")
    String message,

    @Schema(description = "Map of field-specific errors (for validation errors)", example = "{\"email\": \"Email is invalid\"}")
    Map<String, String> errors,

    @Schema(description = "Timestamp of the error", example = "2023-10-01T12:00:00Z")
    String timestamp) {

  public static ErrorResponse of(int status, String message, String timestamp) {
    return new ErrorResponse(status, message, null, timestamp);
  }

  public static ErrorResponse of(int status, String message, Map<String, String> errors, String timestamp) {
    return new ErrorResponse(status, message, errors, timestamp);
  }
}
