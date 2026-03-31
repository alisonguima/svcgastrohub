package com.restaurant.gastrohub.application.port.input;

import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.adapter.input.response.GetUserResponse;
import com.restaurant.gastrohub.application.domain.user.User;

import java.util.List;

public interface UserUseCase {

    CreateUserResponse createUser(User user);
    void updateUser(Long userId, User user);
    void updatePassword(Long userId, User user);
    void deleteUser(Long userId);
    GetUserResponse getUser(Long userId);
    List<GetUserResponse> getUsers(String userType);
}
