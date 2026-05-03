package com.restaurant.gastrohub.adapter.input.request;

import com.restaurant.gastrohub.application.domain.ApiConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for user authentication")
public record LoginRequest(

    @Schema(description = "User's login username", example = "johndoe")
    @NotBlank(message = ApiConstants.LOGIN_REQUIRED_AUTH)
    String login,

    @Schema(description = "User's password", example = "Password123")
    @NotBlank(message = ApiConstants.PASSWORD_REQUIRED_AUTH)
    String password) {
}

