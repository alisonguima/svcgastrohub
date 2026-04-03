package com.restaurant.gastrohub.adapter.input.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(description = "Response payload for user retrieval")
public record GetUserResponse(

    @Schema(description = "User's unique ID", example = "1")
    String id,

    @Schema(description = "User's full name", example = "John Doe")
    String name,

    @Schema(description = "User's email address", example = "john.doe@example.com")
    String email,

    @Schema(description = "User's login username", example = "johndoe")
    String login,

    @Schema(description = "User's type", example = "CUSTOMER")
    UserType userType,

    @Schema(description = "Last update timestamp", example = "2026-04-03T20:50:10Z")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "UTC")
    ZonedDateTime lastUpdateAt,

    @Schema(description = "User's address", example = "123 Main St, City, State")
    String address) {}
