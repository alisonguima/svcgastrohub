package com.restaurant.gastrohub.adapter.input.request;

import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload for updating user details")
public record UpdateUserRequest(

    @Schema(description = "User's full name", example = "Jane Doe")
    @Size(min = 2, max = 100, message = ApiConstants.NAME_SIZE)
    String name,

    @Schema(description = "User's email address", example = "jane.doe@example.com")
    @Email(message = ApiConstants.EMAIL_INVALID)
    String email,

    @Schema(description = "User's login username", example = "janedoe")
    @Size(min = 3, max = 50, message = ApiConstants.LOGIN_SIZE)
    String login,

    @Schema(description = "User's type", example = "OWNER")
    UserType userType,

    @Schema(description = "User's address", example = "456 Elm St, City, State")
    String address
) {}
