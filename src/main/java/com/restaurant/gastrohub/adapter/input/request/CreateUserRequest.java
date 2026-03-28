package com.restaurant.gastrohub.adapter.input.request;

import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record CreateUserRequest(

    @NotBlank(message = ApiConstants.NAME_REQUIRED)
    @Size(min = 2, max = 100, message = ApiConstants.NAME_SIZE)
    String name,

    @NotBlank(message = ApiConstants.EMAIL_REQUIRED)
    @Email(message = ApiConstants.EMAIL_INVALID)
    String email,

    @NotBlank(message = ApiConstants.LOGIN_REQUIRED)
    @Size(min = 3, max = 50, message = ApiConstants.LOGIN_SIZE)
    String login,

    @NotBlank(message = ApiConstants.PASSWORD_REQUIRED)
    @Pattern(
        regexp = ApiConstants.PASSWORD_PATTERN,
        message = ApiConstants.PASSWORD_INVALID)
    String password,

    @NotNull(message = ApiConstants.USER_TYPE_REQUIRED)
    UserType userType,

    @NotBlank(message = ApiConstants.ADDRESS_REQUIRED)
    String address) {
}
