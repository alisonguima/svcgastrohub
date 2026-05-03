package com.restaurant.gastrohub.adapter.input.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload for successful authentication")
public record LoginResponse(

    @Schema(description = "JWT access token to be used in subsequent requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token,

    @Schema(description = "User's unique identifier", example = "1")
    Long userId,

    @Schema(description = "User's login username", example = "johndoe")
    String login,

    @Schema(description = "Type of token (Bearer)", example = "Bearer")
    String type) {

    public LoginResponse(String token, Long userId, String login) {
        this(token, userId, login, "Bearer");
    }
}

