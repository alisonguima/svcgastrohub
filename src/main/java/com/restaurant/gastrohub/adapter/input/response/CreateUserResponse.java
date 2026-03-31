package com.restaurant.gastrohub.adapter.input.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload for user creation")
public record CreateUserResponse(

        @Schema(description = "User's unique ID", example = "1")
        String id,

        @Schema(description = "User's full name", example = "John Doe")
        String name,

        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "User's login username", example = "johndoe")
        String login
) { }
