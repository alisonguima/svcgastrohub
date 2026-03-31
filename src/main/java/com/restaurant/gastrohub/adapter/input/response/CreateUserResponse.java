package com.restaurant.gastrohub.adapter.input.response;

public record CreateUserResponse(

        String id,
        String name,
        String email,
        String login
) { }
