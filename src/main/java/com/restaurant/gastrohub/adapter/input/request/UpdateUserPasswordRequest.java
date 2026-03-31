package com.restaurant.gastrohub.adapter.input.request;

import com.restaurant.gastrohub.application.domain.ApiConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request payload for updating user password")
public record UpdateUserPasswordRequest(

    @Schema(description = "Current password", example = "OldPassword123")
    @NotBlank(message = ApiConstants.CUR_PASSWORD_REQUIRED)
    String currentPassword,

    @Schema(description = "New password (must contain at least 8 characters, one uppercase, one lowercase, one digit)", example = "NewPassword456")
    @NotBlank(message = ApiConstants.NEW_PASSWORD_REQUIRED)
    @Pattern(
        regexp = ApiConstants.PASSWORD_PATTERN,
        message = ApiConstants.PASSWORD_INVALID)
    String newPassword) {
}
