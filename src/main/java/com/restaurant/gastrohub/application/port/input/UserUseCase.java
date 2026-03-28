package com.restaurant.gastrohub.application.port.input;

import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.application.domain.user.User;

public interface UserUseCase {

    CreateUserResponse createUser(User user);

    void updatePassword(Long userId, String currentPassword, String newPassword);
}
