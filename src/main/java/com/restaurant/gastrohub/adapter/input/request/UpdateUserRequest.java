package com.restaurant.gastrohub.adapter.input.request;

import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @Size(min = 2, max = 100, message = ApiConstants.NAME_SIZE)
    String name,

    @Email(message = ApiConstants.EMAIL_INVALID)
    String email,

    @Size(min = 3, max = 50, message = ApiConstants.LOGIN_SIZE)
    String login,

    UserType userType,

    String address
) {}
