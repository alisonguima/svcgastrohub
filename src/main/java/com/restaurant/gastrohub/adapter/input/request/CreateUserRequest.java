package com.restaurant.gastrohub.adapter.input.request;

import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload for creating a new user")
public record CreateUserRequest(

    @Schema(description = "User's full name", example = "John Doe")
    @NotBlank(message = ApiConstants.NAME_REQUIRED)
    @Size(min = 2, max = 100, message = ApiConstants.NAME_SIZE)
    String name,

    @Schema(description = "User's email address", example = "john.doe@example.com")
    @NotBlank(message = ApiConstants.EMAIL_REQUIRED)
    @Email(message = ApiConstants.EMAIL_INVALID)
    String email,

    @Schema(description = "User's login username", example = "johndoe")
    @NotBlank(message = ApiConstants.LOGIN_REQUIRED)
    @Size(min = 3, max = 50, message = ApiConstants.LOGIN_SIZE)
    String login,

    @Schema(description = "User's password (must contain at least 8 characters, one uppercase, one lowercase, one digit)", example = "Password123")
    @NotBlank(message = ApiConstants.PASSWORD_REQUIRED)
    @Pattern(
        regexp = ApiConstants.PASSWORD_PATTERN,
        message = ApiConstants.PASSWORD_INVALID)
    String password,

    @Schema(description = "User's type", example = "CUSTOMER")
    @NotNull(message = ApiConstants.USER_TYPE_REQUIRED)
    UserType userType,

    @Schema(description = "User's address", example = "123 Main St, City, State")
    @NotBlank(message = ApiConstants.ADDRESS_REQUIRED)
    String address) {
}
