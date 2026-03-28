package com.restaurant.gastrohub.adapter.input.response;

import com.restaurant.gastrohub.application.domain.enums.UserType;

public record CreateUserResponse(

        String id,
        String name,
        String email,
        String login,
        UserType userType,
        String address
) { }
