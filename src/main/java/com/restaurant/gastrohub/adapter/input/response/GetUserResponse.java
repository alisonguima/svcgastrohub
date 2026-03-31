package com.restaurant.gastrohub.adapter.input.response;

import com.restaurant.gastrohub.application.domain.enums.UserType;

public record GetUserResponse(
    String id,
    String name,
    String email,
    String login,
    UserType userType,
    String lastUpdateAt,
    String address) {}
