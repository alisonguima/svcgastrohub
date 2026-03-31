package com.restaurant.gastrohub.adapter.input.response;

import com.restaurant.gastrohub.application.domain.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

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

    @Schema(description = "Last update timestamp", example = "2023-10-01T12:00:00Z")
    String lastUpdateAt,

    @Schema(description = "User's address", example = "123 Main St, City, State")
    String address) {}
